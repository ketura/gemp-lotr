package com.gempukku.lotro.async.handler;

import com.alibaba.fastjson.JSON;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.vo.LotroDeck;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;


import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class DeckRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private DeckDAO _deckDao;
    private SortAndFilterCards _sortAndFilterCards;
    private LotroCardBlueprintLibrary _library;
    private LotroFormatLibrary _formatLibrary;
    private LotroServer _lotroServer;

    public DeckRequestHandler(Map<Type, Object> context) {
        super(context);
        _deckDao = extractObject(context, DeckDAO.class);
        _sortAndFilterCards = new SortAndFilterCards();
        _library = extractObject(context, LotroCardBlueprintLibrary.class);
        _formatLibrary = extractObject(context, LotroFormatLibrary.class);
        _lotroServer = extractObject(context, LotroServer.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.equals("/list") && request.method() == HttpMethod.GET) {
            listDecks(request, responseWriter);
        } else if (uri.equals("/libraryList") && request.method() == HttpMethod.GET) {
            listLibraryDecks(request, responseWriter);
        } else if (uri.equals("") && request.method() == HttpMethod.GET) {
            getDeck(request, responseWriter);
        } else if (uri.equals("") && request.method() == HttpMethod.POST) {
            saveDeck(request, responseWriter);
        } else if (uri.equals("/library") && request.method() == HttpMethod.GET) {
            getLibraryDeck(request, responseWriter);
        } else if (uri.equals("/html") && request.method() == HttpMethod.GET) {
            getDeckInHtml(request, responseWriter);
        } else if (uri.equals("/libraryHtml") && request.method() == HttpMethod.GET) {
            getLibraryDeckInHtml(request, responseWriter);
        } else if (uri.equals("/rename") && request.method() == HttpMethod.POST) {
            renameDeck(request, responseWriter);
        } else if (uri.equals("/delete") && request.method() == HttpMethod.POST) {
            deleteDeck(request, responseWriter);
        } else if (uri.equals("/stats") && request.method() == HttpMethod.POST) {
            getDeckStats(request, responseWriter);
        } else if (uri.equals("/formats") && request.method() == HttpMethod.GET) {
            getAllFormats(request, responseWriter);
        } else {
            throw new HttpProcessingException(404);
        }
    }

    public class Format {
        public String code;
        public String name;
        public Format(String c, String n) {
            code = c;
            name = n;
        }
    }

    private void getAllFormats(HttpRequest request, ResponseWriter responseWriter) {

        Map<String, LotroFormat> formats = _formatLibrary.getHallFormats();
        Object[] formats2 = formats.entrySet().stream()
                .map(x -> new Format(x.getKey(), x.getValue().getName()))
                .toArray();

        String json = JSON.toJSONString(formats2);
        responseWriter.writeJsonResponse(json);
    }

    private void getDeckStats(HttpRequest request, ResponseWriter responseWriter) throws IOException, HttpProcessingException, CardNotFoundException {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
            String participantId = getFormParameterSafely(postDecoder, "participantId");
            String targetFormat = getFormParameterSafely(postDecoder, "targetFormat");
            String contents = getFormParameterSafely(postDecoder, "deckContents");

            //check for valid access
            getResourceOwnerSafely(request, participantId);

            LotroDeck deck = _lotroServer.createDeckWithValidate("tempDeck", contents, targetFormat);
            if (deck == null)
                throw new HttpProcessingException(400);

            int fpCount = 0;
            int shadowCount = 0;
            for (String card : deck.getAdventureCards()) {
                Side side = _library.getLotroCardBlueprint(card).getSide();
                if (side == Side.SHADOW)
                    shadowCount++;
                else if (side == Side.FREE_PEOPLE)
                    fpCount++;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<b>Free People</b>: " + fpCount + ", <b>Shadow</b>: " + shadowCount + "<br/>");

            StringBuilder valid = new StringBuilder();
            StringBuilder invalid = new StringBuilder();

            LotroFormat format = _formatLibrary.getHallFormats().get(targetFormat);
            if(format == null || targetFormat == null)
            {
                responseWriter.writeHtmlResponse("Invalid format: " + targetFormat);
            }

            List<String> validation = format.validateDeck(deck);
            List<String> errataValidation = null;
            if (!format.getErrataCardMap().isEmpty()) {
                LotroDeck deckWithErrata = format.applyErrata(deck);
                errataValidation = format.validateDeck(deckWithErrata);
            }
            if(validation.size() == 0) {
                valid.append("<b>" + format.getName() + "</b>: <font color='green'>Valid</font><br/>");
            }
            else if(errataValidation != null && errataValidation.size() == 0) {
                valid.append("<b>" + format.getName() + "</b>: <font color='green'>Valid</font> <font color='yellow'>(with errata automatically applied)</font><br/>");
                String output = String.join("<br>", validation).replace("\n", "<br>");
                invalid.append("<font color='yellow'>" + output + "</font><br/>");
            }
            else {
                String output = String.join("<br>", validation).replace("\n", "<br>");
                invalid.append("<b>" + format.getName() + "</b>: <font color='red'>" + output + "</font><br/>");
            }

            sb.append(valid);
            sb.append(invalid);

            responseWriter.writeHtmlResponse(sb.toString());
        } finally {
            postDecoder.destroy();
        }
    }

    private void deleteDeck(HttpRequest request, ResponseWriter responseWriter) throws IOException, HttpProcessingException {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
            String participantId = getFormParameterSafely(postDecoder, "participantId");
            String deckName = getFormParameterSafely(postDecoder, "deckName");
            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            _deckDao.deleteDeckForPlayer(resourceOwner, deckName);

            responseWriter.writeXmlResponse(null);
        } finally {
            postDecoder.destroy();
        }
    }

    private void renameDeck(HttpRequest request, ResponseWriter responseWriter) throws IOException, HttpProcessingException, ParserConfigurationException {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
            String participantId = getFormParameterSafely(postDecoder, "participantId");
            String deckName = getFormParameterSafely(postDecoder, "deckName");
            String oldDeckName = getFormParameterSafely(postDecoder, "oldDeckName");

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            LotroDeck deck = _deckDao.renameDeck(resourceOwner, oldDeckName, deckName);
            if (deck == null)
                throw new HttpProcessingException(404);

            responseWriter.writeXmlResponse(serializeDeck(deck));
        } finally {
            postDecoder.destroy();
        }
    }

    private void saveDeck(HttpRequest request, ResponseWriter responseWriter) throws IOException, HttpProcessingException, ParserConfigurationException {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
            String participantId = getFormParameterSafely(postDecoder, "participantId");
            String deckName = getFormParameterSafely(postDecoder, "deckName");
            String targetFormat = getFormParameterSafely(postDecoder, "targetFormat");
            String contents = getFormParameterSafely(postDecoder, "deckContents");

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            LotroDeck lotroDeck = _lotroServer.createDeckWithValidate(deckName, contents, targetFormat);
            if (lotroDeck == null)
                throw new HttpProcessingException(400);

            _deckDao.saveDeckForPlayer(resourceOwner, deckName, targetFormat, lotroDeck);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element deckElem = doc.createElement("ok");
            doc.appendChild(deckElem);

            responseWriter.writeXmlResponse(doc);
        } finally {
            postDecoder.destroy();
        }
    }

    private void getDeckInHtml(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, CardNotFoundException {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");
        String deckName = getQueryParameterSafely(queryDecoder, "deckName");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _deckDao.getDeckForPlayer(resourceOwner, deckName);

        if (deck == null)
            throw new HttpProcessingException(404);

        String result = convertDeckToHTML(deck);

        responseWriter.writeHtmlResponse(result);
    }

    private void getLibraryDeckInHtml(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, CardNotFoundException {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        String deckName = getQueryParameterSafely(queryDecoder, "deckName");

        LotroDeck deck = _deckDao.getDeckForPlayer(getLibrarian(), deckName);

        if (deck == null)
            throw new HttpProcessingException(404);

        String result = convertDeckToHTML(deck);

        responseWriter.writeHtmlResponse(result);
    }

    public String convertDeckToHTML(LotroDeck deck) throws CardNotFoundException {

        if (deck == null)
            return null;

        StringBuilder result = new StringBuilder();
        result.append("<html><body>");
        result.append("<h1>" + StringEscapeUtils.escapeHtml(deck.getDeckName()) + "</h1>");
        result.append("<h2>Format: " + StringEscapeUtils.escapeHtml(deck.getTargetFormat()) + "</h2>");
        String ringBearer = deck.getRingBearer();
        if (ringBearer != null)
            result.append("<b>Ring-bearer:</b> " + GameUtils.getFullName(_library.getLotroCardBlueprint(ringBearer)) + "<br/>");
        String ring = deck.getRing();
        if (ring != null)
            result.append("<b>Ring:</b> " + GameUtils.getFullName(_library.getLotroCardBlueprint(ring)) + "<br/>");

        DefaultCardCollection deckCards = new DefaultCardCollection();
        for (String card : deck.getAdventureCards())
            deckCards.addItem(_library.getBaseBlueprintId(card), 1);
        for (String site : deck.getSites())
            deckCards.addItem(_library.getBaseBlueprintId(site), 1);

        result.append("<br/>");
        result.append("<b>Adventure deck:</b><br/>");
        for (CardCollection.Item item : _sortAndFilterCards.process("cardType:SITE sort:siteNumber,twilight", deckCards.getAll(), _library, _formatLibrary, null))
            result.append(GameUtils.getFullName(_library.getLotroCardBlueprint(item.getBlueprintId())) + "<br/>");

        result.append("<br/>");
        result.append("<b>Free Peoples Draw Deck:</b><br/>");
        for (CardCollection.Item item : _sortAndFilterCards.process("side:FREE_PEOPLE sort:cardType,culture,name", deckCards.getAll(), _library, _formatLibrary, null))
            result.append(item.getCount() + "x " + GameUtils.getFullName(_library.getLotroCardBlueprint(item.getBlueprintId())) + "<br/>");

        result.append("<br/>");
        result.append("<b>Shadow Draw Deck:</b><br/>");
        for (CardCollection.Item item : _sortAndFilterCards.process("side:SHADOW sort:cardType,culture,name", deckCards.getAll(), _library, _formatLibrary, null))
            result.append(item.getCount() + "x " + GameUtils.getFullName(_library.getLotroCardBlueprint(item.getBlueprintId())) + "<br/>");

        result.append("</body></html>");

        return result.toString();
    }

    private void getDeck(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, ParserConfigurationException {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");
        String deckName = getQueryParameterSafely(queryDecoder, "deckName");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        responseWriter.writeXmlResponse(serializeDeck(resourceOwner, deckName));
    }

    private void getLibraryDeck(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, ParserConfigurationException {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        String deckName = getQueryParameterSafely(queryDecoder, "deckName");

        responseWriter.writeXmlResponse(serializeDeck(getLibrarian(), deckName));
    }

    private void listDecks(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, ParserConfigurationException {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        List<Map.Entry<LotroFormat, String>> decks = GetDeckNamesAndFormats(resourceOwner);
        SortDecks(decks);

        Document doc = ConvertDeckNamesToXML(decks);
        responseWriter.writeXmlResponse(doc);
    }

    private Document ConvertDeckNamesToXML(List<Map.Entry<LotroFormat, String>> deckNames) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element decksElem = doc.createElement("decks");

        for (Map.Entry<LotroFormat, String> pair : deckNames) {
            Element deckElem = doc.createElement("deck");
            deckElem.setTextContent(pair.getValue());
            deckElem.setAttribute("targetFormat", pair.getKey().getName());
            decksElem.appendChild(deckElem);
        }
        doc.appendChild(decksElem);
        return doc;
    }

    private List<Map.Entry<LotroFormat, String>> GetDeckNamesAndFormats(Player player)
    {
        Set<Map.Entry<String, String>> names = new HashSet(_deckDao.getPlayerDeckNames(player));

        return names.stream()
                .map(pair -> new AbstractMap.SimpleEntry<>(_formatLibrary.getFormatByName(pair.getKey()), pair.getValue()))
                .collect(Collectors.toList());
    }

    private void SortDecks(List<Map.Entry<LotroFormat, String>> decks)
    {
        decks.sort(Comparator.comparing((deck) -> {
            LotroFormat format = deck.getKey();
            return String.format("%02d", format.getOrder()) + format.getName() + deck.getValue();
        }));
    }

    private void listLibraryDecks(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, ParserConfigurationException {
        List<Map.Entry<LotroFormat, String>> starterDecks = new ArrayList<>();
        List<Map.Entry<LotroFormat, String>> championshipDecks = new ArrayList<>();

        List<Map.Entry<LotroFormat, String>> decks = GetDeckNamesAndFormats(getLibrarian());

        for (Map.Entry<LotroFormat, String> pair : decks) {

            if (pair.getValue().contains("Starter"))
                starterDecks.add(pair);
            else
                championshipDecks.add(pair);
        }

        SortDecks(starterDecks);
        SortDecks(championshipDecks);

        //Keeps all the championship decks at the bottom of the list
        starterDecks.addAll(championshipDecks);

        Document doc = ConvertDeckNamesToXML(starterDecks);

        // Write the XML response
        responseWriter.writeXmlResponse(doc);
    }

    private Document serializeDeck(Player player, String deckName) throws ParserConfigurationException {
        LotroDeck deck = _deckDao.getDeckForPlayer(player, deckName);

        return serializeDeck(deck);
    }

    private Document serializeDeck(LotroDeck deck) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("deck");
        doc.appendChild(deckElem);

        if (deck == null)
            return doc;

        Element targetFormat = doc.createElement("targetFormat");
        targetFormat.setAttribute("formatName", deck.getTargetFormat());
        deckElem.appendChild(targetFormat);

        if (deck.getRingBearer() != null) {
            Element ringBearer = doc.createElement("ringBearer");
            ringBearer.setAttribute("blueprintId", deck.getRingBearer());
            deckElem.appendChild(ringBearer);
        }

        if (deck.getRing() != null) {
            Element ring = doc.createElement("ring");
            ring.setAttribute("blueprintId", deck.getRing());
            deckElem.appendChild(ring);
        }

        for (CardItem cardItem : _sortAndFilterCards.process("sort:siteNumber,twilight", createCardItems(deck.getSites()), _library, _formatLibrary, null)) {
            Element site = doc.createElement("site");
            site.setAttribute("blueprintId", cardItem.getBlueprintId());
            deckElem.appendChild(site);
        }

        for (CardItem cardItem : _sortAndFilterCards.process("sort:cardType,culture,name", createCardItems(deck.getAdventureCards()), _library, _formatLibrary, null)) {
            Element card = doc.createElement("card");
            String side;
            try {
                side = _library.getLotroCardBlueprint(cardItem.getBlueprintId()).getSide().toString();
            } catch (CardNotFoundException e) {
                side = "FREE_PEOPLE";
            }
            card.setAttribute("side", side);
            card.setAttribute("blueprintId", cardItem.getBlueprintId());
            deckElem.appendChild(card);
        }

        return doc;
    }

    private List<CardItem> createCardItems(List<String> blueprintIds) {
        List<CardItem> cardItems = new LinkedList<CardItem>();
        for (String blueprintId : blueprintIds)
            cardItems.add(new BasicCardItem(blueprintId));

        return cardItems;
    }
}
