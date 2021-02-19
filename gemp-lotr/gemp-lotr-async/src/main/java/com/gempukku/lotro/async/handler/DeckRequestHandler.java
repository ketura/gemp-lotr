package com.gempukku.lotro.async.handler;

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
import java.lang.reflect.Type;
import java.util.*;

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
        if (uri.equals("/list") && request.getMethod() == HttpMethod.GET) {
            listDecks(request, responseWriter);
        } else if (uri.equals("") && request.getMethod() == HttpMethod.GET) {
            getDeck(request, responseWriter);
        } else if (uri.equals("") && request.getMethod() == HttpMethod.POST) {
            saveDeck(request, responseWriter);
        } else if (uri.equals("/html") && request.getMethod() == HttpMethod.GET) {
            getDeckInHtml(request, responseWriter);
        } else if (uri.equals("/rename") && request.getMethod() == HttpMethod.POST) {
            renameDeck(request, responseWriter);
        } else if (uri.equals("/delete") && request.getMethod() == HttpMethod.POST) {
            deleteDeck(request, responseWriter);
        } else if (uri.equals("/stats") && request.getMethod() == HttpMethod.POST) {
            getDeckStats(request, responseWriter);
        } else {
            throw new HttpProcessingException(404);
        }
    }

    private void getDeckStats(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String contents = getFormParameterSafely(postDecoder, "deckContents");
        
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _lotroServer.createDeckWithValidate("tempDeck", contents);
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
        for (LotroFormat format : _formatLibrary.getHallFormats().values()) {
            try {
                format.validateDeck(deck);
                valid.append("<b>" + format.getName() + "</b>: <font color='green'>valid</font><br/>");
            } catch (DeckInvalidException exp) {
                invalid.append("<b>" + format.getName() + "</b>: <font color='red'>" + exp.getMessage() + "</font><br/>");
            }
        }
        sb.append(valid);
        sb.append(invalid);

        responseWriter.writeHtmlResponse(sb.toString());
        } finally {
            postDecoder.destroy();
        }
    }

    private void deleteDeck(HttpRequest request, ResponseWriter responseWriter) throws Exception {
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

    private void renameDeck(HttpRequest request, ResponseWriter responseWriter) throws Exception {
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

    private void saveDeck(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String deckName = getFormParameterSafely(postDecoder, "deckName");
        String contents = getFormParameterSafely(postDecoder, "deckContents");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck lotroDeck = _lotroServer.createDeckWithValidate(deckName, contents);
        if (lotroDeck == null)
            throw new HttpProcessingException(400);

        _deckDao.saveDeckForPlayer(resourceOwner, deckName, lotroDeck);

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

    private void getDeckInHtml(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");
        String deckName = getQueryParameterSafely(queryDecoder, "deckName");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _deckDao.getDeckForPlayer(resourceOwner, deckName);

        if (deck == null)
            throw new HttpProcessingException(404);

        StringBuilder result = new StringBuilder();
        result.append("<html><body>");
        result.append("<h1>" + StringEscapeUtils.escapeHtml(deck.getDeckName()) + "</h1>");
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

        responseWriter.writeHtmlResponse(result.toString());
    }

    private void getDeck(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");
        String deckName = getQueryParameterSafely(queryDecoder, "deckName");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _deckDao.getDeckForPlayer(resourceOwner, deckName);

        if (deck == null) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element deckElem = doc.createElement("deck");
            doc.appendChild(deckElem);

            responseWriter.writeXmlResponse(doc);
        } else {
            responseWriter.writeXmlResponse(serializeDeck(deck));
        }
    }

    private void listDecks(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        List<String> names = new ArrayList<String>(_deckDao.getPlayerDeckNames(resourceOwner));
        Collections.sort(names);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element decksElem = doc.createElement("decks");
        for (String name : names) {
            Element deckElem = doc.createElement("deck");
            deckElem.appendChild(doc.createTextNode(name));
            decksElem.appendChild(deckElem);
        }
        doc.appendChild(decksElem);
        responseWriter.writeXmlResponse(doc);
    }

    private Document serializeDeck(LotroDeck deck) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("deck");
        doc.appendChild(deckElem);

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
