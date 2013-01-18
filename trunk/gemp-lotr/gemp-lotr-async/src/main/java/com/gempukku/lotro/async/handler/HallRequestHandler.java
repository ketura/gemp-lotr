package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.SubscriptionConflictException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.LongPollingResource;
import com.gempukku.lotro.async.LongPollingSystem;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.draft.DraftChannelVisitor;
import com.gempukku.lotro.draft.DraftFinishedException;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallChannelVisitor;
import com.gempukku.lotro.hall.HallException;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.vo.LotroDeck;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HallRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private CollectionsManager _collectionManager;
    private LotroFormatLibrary _formatLibrary;
    private HallServer _hallServer;
    private LeagueService _leagueService;
    private LotroCardBlueprintLibrary _library;
    private LongPollingSystem _longPollingSystem;
    private LotroServer _lotroServer;

    public HallRequestHandler(Map<Type, Object> context) {
        super(context);
        _collectionManager = extractObject(context, CollectionsManager.class);
        _formatLibrary = extractObject(context, LotroFormatLibrary.class);
        _hallServer = extractObject(context, HallServer.class);
        _leagueService = extractObject(context, LeagueService.class);
        _library = extractObject(context, LotroCardBlueprintLibrary.class);
        _longPollingSystem = extractObject(context, LongPollingSystem.class);
        _lotroServer = extractObject(context, LotroServer.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.equals("") && request.getMethod() == HttpMethod.GET) {
            getHall(request, responseWriter);
        } else if (uri.equals("") && request.getMethod() == HttpMethod.POST) {
            createTable(request, responseWriter);
        } else if (uri.equals("/update") && request.getMethod() == HttpMethod.POST) {
            updateHall(request, responseWriter);
        } else if (uri.startsWith("/draft/") && uri.endsWith("/update") && request.getMethod() == HttpMethod.POST) {
            updateDraft(request, uri.substring(7, uri.length() - 7), responseWriter);
        } else if (uri.startsWith("/draft/") && uri.endsWith("/pick") && request.getMethod() == HttpMethod.POST) {
            draftPick(request, uri.substring(7, uri.length() - 5), responseWriter);
        } else if (uri.startsWith("/draft/") && request.getMethod() == HttpMethod.GET) {
            getDraft(request, uri.substring(7), responseWriter);
        } else if (uri.equals("/formats/html") && request.getMethod() == HttpMethod.GET) {
            getFormats(request, responseWriter);
        } else if (uri.startsWith("/format/") && request.getMethod() == HttpMethod.GET) {
            getFormat(request, uri.substring(8), responseWriter);
        } else if (uri.startsWith("/queue/") && request.getMethod() == HttpMethod.POST) {
            if (uri.endsWith("/leave")) {
                leaveQueue(request, uri.substring(7, uri.length() - 6), responseWriter);
            } else {
                joinQueue(request, uri.substring(7), responseWriter);
            }
        } else if (uri.startsWith("/tournament/") && uri.endsWith("/deck") && request.getMethod() == HttpMethod.POST) {
            submitTournamentDeck(request, uri.substring(12, uri.length() - 5), responseWriter);
        } else if (uri.startsWith("/tournament/") && uri.endsWith("/leave") && request.getMethod() == HttpMethod.POST) {
            dropFromTournament(request, uri.substring(12, uri.length() - 6), responseWriter);
        } else if (uri.startsWith("/") && uri.endsWith("/leave") && request.getMethod() == HttpMethod.POST) {
            leaveTable(request, uri.substring(1, uri.length()-6), responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.POST) {
            joinTable(request, uri.substring(1), responseWriter);
        } else {
            responseWriter.writeError(404);
        }
    }

    private void submitTournamentDeck(HttpRequest request, String tournamentId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String contents = getFormParameterSafely(postDecoder, "deckContents");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck lotroDeck = _lotroServer.createDeckWithValidate("Limited deck", contents);
        if (lotroDeck == null)
            throw new HttpProcessingException(400);

        try {
            _hallServer.submitTournamentDeck(tournamentId, resourceOwner, lotroDeck);
            responseWriter.writeXmlResponse(null);
        } catch (HallException e) {
            responseWriter.writeXmlResponse(marshalException(e));
        }
    }

    private void draftPick(HttpRequest request, String tournamentId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String blueprintId = getFormParameterSafely(postDecoder, "blueprintId");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            _hallServer.draftPick(tournamentId, resourceOwner, blueprintId);
            responseWriter.writeXmlResponse(null);
        } catch (DraftFinishedException exp) {
            responseWriter.writeError(204);
        }
    }

    private void updateDraft(HttpRequest request, String tournamentId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        int channelNumber = Integer.parseInt(getFormParameterSafely(postDecoder, "channelNumber"));
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DraftUpdateLongPollingResource polledResource = new DraftUpdateLongPollingResource(tournamentId, resourceOwner, channelNumber, responseWriter);
        if (polledResource.isChanged())
            polledResource.process();
        else
            _longPollingSystem.appendLongPollingResource(polledResource);
    }

    private class DraftUpdateLongPollingResource implements LongPollingResource {
        private Player _player;
        private int _channelNumber;
        private String _tournamentId;
        private ResponseWriter _responseWriter;

        private DraftUpdateLongPollingResource(String tournamentId, Player player, int channelNumber, ResponseWriter responseWriter) {
            _tournamentId = tournamentId;
            _player = player;
            _channelNumber = channelNumber;
            _responseWriter = responseWriter;
        }

        public boolean isChanged() {
            try {
                return _hallServer.hasChangesInDraft(_tournamentId, _player, _channelNumber);
            } catch (DraftFinishedException e) {
                return true;
            } catch (SubscriptionConflictException e) {
                return true;
            } catch (SubscriptionExpiredException e) {
                return true;
            }
        }

        public void process() {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document doc = documentBuilder.newDocument();

                Element draft = doc.createElement("draft");

                try {
                    _hallServer.processChangesInDraft(_tournamentId, _player, _channelNumber, new SerializeDraftVisitor(doc, draft));

                    doc.appendChild(draft);

                    _responseWriter.writeXmlResponse(doc);
                } catch (DraftFinishedException e) {
                    throw new HttpProcessingException(204);
                } catch (SubscriptionConflictException e) {
                    throw new HttpProcessingException(409);
                } catch (SubscriptionExpiredException e) {
                    throw new HttpProcessingException(410);
                }
            } catch (HttpProcessingException exp) {
                _responseWriter.writeError(exp.getStatus());
            } catch (Exception exp) {
                _responseWriter.writeError(500);
            }
        }
    }

    private void getDraft(HttpRequest request, String tournamentId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element draft = doc.createElement("draft");

        try {
            _hallServer.singupForDraft(tournamentId, resourceOwner, new SerializeDraftVisitor(doc, draft));

            doc.appendChild(draft);

            responseWriter.writeXmlResponse(doc);
        } catch (DraftFinishedException exp) {
            responseWriter.writeError(204);
        }
    }

    private void joinTable(HttpRequest request, String tableId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        String deckName = getFormParameterSafely(postDecoder, "deckName");

        try {
            _hallServer.joinTableAsPlayer(tableId, resourceOwner, deckName);
            responseWriter.writeXmlResponse(null);
        } catch (HallException e) {
            responseWriter.writeXmlResponse(marshalException(e));
        }
    }

    private void leaveTable(HttpRequest request, String tableId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _hallServer.leaveAwaitingTable(resourceOwner, tableId);
        responseWriter.writeXmlResponse(null);
    }

    private void createTable(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String format = getFormParameterSafely(postDecoder, "format");
        String deckName = getFormParameterSafely(postDecoder, "deckName");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            _hallServer.createNewTable(format, resourceOwner, deckName);
            responseWriter.writeXmlResponse(null);
        } catch (HallException e) {
            responseWriter.writeXmlResponse(marshalException(e));
        }
    }

    private void dropFromTournament(HttpRequest request, String tournamentId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _hallServer.dropFromTournament(tournamentId, resourceOwner);

        responseWriter.writeXmlResponse(null);
    }

    private void joinQueue(HttpRequest request, String queueId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String deckName = getFormParameterSafely(postDecoder, "deckName");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            _hallServer.joinQueue(queueId, resourceOwner, deckName);
            responseWriter.writeXmlResponse(null);
        } catch (HallException e) {
            responseWriter.writeXmlResponse(marshalException(e));
        }
    }

    private void leaveQueue(HttpRequest request, String queueId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _hallServer.leaveQueue(queueId, resourceOwner);

        responseWriter.writeXmlResponse(null);
    }

    private Document marshalException(HallException e) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element error = doc.createElement("error");
        error.setAttribute("message", e.getMessage());
        doc.appendChild(error);
        return doc;
    }

    private void getFormat(HttpRequest request, String format, ResponseWriter responseWriter) {
        StringBuilder result = new StringBuilder();
        LotroFormat lotroFormat = _formatLibrary.getFormat(format);
        result.append("<b>" + lotroFormat.getName() + "</b>");
        result.append("<ul>");
        result.append("<li>valid sets: ");
        for (Integer integer : lotroFormat.getValidSets())
            result.append(integer + ", ");
        result.append("</li>");
        result.append("<li>sites from block: " + lotroFormat.getSiteBlock().getHumanReadable() + "</li>");
        result.append("<li>Ring-bearer skirmish can be cancelled: " + (lotroFormat.canCancelRingBearerSkirmish() ? "yes" : "no") + "</li>");
        result.append("<li>X-listed: ");
        for (String blueprintId : lotroFormat.getBannedCards())
            result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
        if (lotroFormat.getBannedCards().size() == 0)
            result.append("none,");
        result.append("</li>");
        result.append("<li>R-listed: ");
        for (String blueprintId : lotroFormat.getRestrictedCards())
            result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
        if (lotroFormat.getRestrictedCards().size() == 0)
            result.append("none,");
        result.append("</li>");
        result.append("<li>Additional valid: ");
        for (String blueprintId : lotroFormat.getValidCards())
            result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
        if (lotroFormat.getValidCards().size() == 0)
            result.append("none,");
        result.append("</li>");
        result.append("</ul>");

        responseWriter.writeHtmlResponse(result.toString());
    }

    private void getFormats(HttpRequest request, ResponseWriter responseWriter) {
        StringBuilder result = new StringBuilder();
        for (LotroFormat lotroFormat : _formatLibrary.getHallFormats().values()) {
            result.append("<b>" + lotroFormat.getName() + "</b>");
            result.append("<ul>");
            result.append("<li>valid sets: ");
            for (Integer integer : lotroFormat.getValidSets())
                result.append(integer + ", ");
            result.append("</li>");
            result.append("<li>sites from block: " + lotroFormat.getSiteBlock().getHumanReadable() + "</li>");
            result.append("<li>Ring-bearer skirmish can be cancelled: " + (lotroFormat.canCancelRingBearerSkirmish() ? "yes" : "no") + "</li>");
            result.append("<li>X-listed: ");
            for (String blueprintId : lotroFormat.getBannedCards())
                result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
            if (lotroFormat.getBannedCards().size() == 0)
                result.append("none,");
            result.append("</li>");
            result.append("<li>R-listed: ");
            for (String blueprintId : lotroFormat.getRestrictedCards())
                result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
            if (lotroFormat.getRestrictedCards().size() == 0)
                result.append("none,");
            result.append("</li>");
            result.append("<li>Additional valid: ");
            for (String blueprintId : lotroFormat.getValidCards())
                result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
            if (lotroFormat.getValidCards().size() == 0)
                result.append("none,");
            result.append("</li>");
            result.append("</ul>");
        }

        responseWriter.writeHtmlResponse(result.toString());
    }

    private void getHall(HttpRequest request, ResponseWriter responseWriter) {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());

        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        try {
            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            Element hall = doc.createElement("hall");
            hall.setAttribute("currency", String.valueOf(_collectionManager.getPlayerCollection(resourceOwner, "permanent").getCurrency()));

            _hallServer.signupUserForHall(resourceOwner, new SerializeHallInfoVisitor(doc, hall));
            for (Map.Entry<String, LotroFormat> format : _formatLibrary.getHallFormats().entrySet()) {
                Element formatElem = doc.createElement("format");
                formatElem.setAttribute("type", format.getKey());
                formatElem.appendChild(doc.createTextNode(format.getValue().getName()));
                hall.appendChild(formatElem);
            }
            for (League league : _leagueService.getActiveLeagues()) {
                final LeagueSerieData currentLeagueSerie = _leagueService.getCurrentLeagueSerie(league);
                if (currentLeagueSerie != null && _leagueService.isPlayerInLeague(league, resourceOwner)) {
                    Element formatElem = doc.createElement("format");
                    formatElem.setAttribute("type", league.getType());
                    formatElem.appendChild(doc.createTextNode(league.getName()));
                    hall.appendChild(formatElem);
                }
            }

            doc.appendChild(hall);

            responseWriter.writeXmlResponse(doc);
        } catch (HttpProcessingException exp) {
            responseWriter.writeError(exp.getStatus());
        } catch (Exception exp) {
            responseWriter.writeError(500);
        }
    }

    private void updateHall(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        int channelNumber = Integer.parseInt(getFormParameterSafely(postDecoder, "channelNumber"));

        Player resourceOwner = getResourceOwnerSafely(request, participantId);
        processLoginReward(resourceOwner.getName());

        HallUpdateLongPollingResource polledResource = new HallUpdateLongPollingResource(request, resourceOwner, channelNumber, responseWriter);
        if (polledResource.isChanged())
            polledResource.process();
        else
            _longPollingSystem.appendLongPollingResource(polledResource);
    }

    private class HallUpdateLongPollingResource implements LongPollingResource {
        private HttpRequest _request;
        private Player _resourceOwner;
        private int _channelNumber;
        private ResponseWriter _responseWriter;

        private HallUpdateLongPollingResource(HttpRequest request, Player resourceOwner, int channelNumber, ResponseWriter responseWriter) {
            _request = request;
            _resourceOwner = resourceOwner;
            _channelNumber = channelNumber;
            _responseWriter = responseWriter;
        }

        @Override
        public boolean isChanged() {
            try {
                return _hallServer.hasChanges(_resourceOwner, _channelNumber);
            } catch (SubscriptionExpiredException e) {
                return true;
            } catch (SubscriptionConflictException e) {
                return true;
            }
        }

        @Override
        public void process() {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document doc = documentBuilder.newDocument();

                Element hall = doc.createElement("hall");
                try {
                    _hallServer.processHall(_resourceOwner, _channelNumber, new SerializeHallInfoVisitor(doc, hall));
                } catch (SubscriptionExpiredException exp) {
                    throw new HttpProcessingException(410);
                } catch (SubscriptionConflictException exp) {
                    throw new HttpProcessingException(409);
                }
                hall.setAttribute("currency", String.valueOf(_collectionManager.getPlayerCollection(_resourceOwner, "permanent").getCurrency()));

                doc.appendChild(hall);

                Map<String, String> headers = new HashMap<String, String>();
                processDeliveryServiceNotification(_request, headers);

                _responseWriter.writeXmlResponse(doc, headers);
            } catch (HttpProcessingException exp) {
                _responseWriter.writeError(exp.getStatus());
            } catch (Exception exp) {
                _responseWriter.writeError(500);
            }
        }
    }

    private class SerializeDraftVisitor implements DraftChannelVisitor {
        private Document _doc;
        private Element _draft;

        private SerializeDraftVisitor(Document doc, Element draft) {
            _doc = doc;
            _draft = draft;
        }

        public void channelNumber(int channelNumber) {
            _draft.setAttribute("channelNumber", String.valueOf(channelNumber));
        }

        public void timeLeft(long timeLeft) {
            _draft.setAttribute("timeLeft", String.valueOf(timeLeft));
        }

        public void noCardChoice() {
        }

        public void cardChoice(CardCollection cardCollection) {
            for (CardCollection.Item possiblePick : cardCollection.getAll().values()) {
                for (int i = 0; i < possiblePick.getCount(); i++) {
                    Element pick = _doc.createElement("pick");
                    pick.setAttribute("blueprintId", possiblePick.getBlueprintId());
                    _draft.appendChild(pick);
                }
            }
        }

        public void chosenCards(CardCollection cardCollection) {
            for (CardCollection.Item cardInCollection : cardCollection.getAll().values()) {
                Element card = _doc.createElement("card");
                card.setAttribute("blueprintId", cardInCollection.getBlueprintId());
                card.setAttribute("count", String.valueOf(cardInCollection.getCount()));
                _draft.appendChild(card);
            }
        }
    }

    private class SerializeHallInfoVisitor implements HallChannelVisitor {
        private Document _doc;
        private Element _hall;

        public SerializeHallInfoVisitor(Document doc, Element hall) {
            _doc = doc;
            _hall = hall;
        }

        @Override
        public void channelNumber(int channelNumber) {
            _hall.setAttribute("channelNumber", String.valueOf(channelNumber));
        }

        @Override
        public void newPlayerGame(String gameId) {
            Element newGame = _doc.createElement("newGame");
            newGame.setAttribute("id", gameId);
            _hall.appendChild(newGame);
        }

        @Override
        public void serverTime(String serverTime) {
            _hall.setAttribute("serverTime", serverTime);
        }

        @Override
        public void motdChanged(String motd) {
            _hall.setAttribute("motd", motd);
        }

        @Override
        public void addTournamentQueue(String queueId, Map<String, String> props) {
            Element queue = _doc.createElement("queue");
            queue.setAttribute("action", "add");
            queue.setAttribute("id", queueId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                queue.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(queue);
        }

        @Override
        public void updateTournamentQueue(String queueId, Map<String, String> props) {
            Element queue = _doc.createElement("queue");
            queue.setAttribute("action", "update");
            queue.setAttribute("id", queueId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                queue.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(queue);
        }

        @Override
        public void removeTournamentQueue(String queueId) {
            Element queue = _doc.createElement("queue");
            queue.setAttribute("action", "remove");
            queue.setAttribute("id", queueId);
            _hall.appendChild(queue);
        }

        @Override
        public void addTournament(String tournamentId, Map<String, String> props) {
            Element tournament = _doc.createElement("tournament");
            tournament.setAttribute("action", "add");
            tournament.setAttribute("id", tournamentId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                tournament.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(tournament);
        }

        @Override
        public void updateTournament(String tournamentId, Map<String, String> props) {
            Element tournament = _doc.createElement("tournament");
            tournament.setAttribute("action", "update");
            tournament.setAttribute("id", tournamentId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                tournament.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(tournament);
        }

        @Override
        public void removeTournament(String tournamentId) {
            Element tournament = _doc.createElement("tournament");
            tournament.setAttribute("action", "remove");
            tournament.setAttribute("id", tournamentId);
            _hall.appendChild(tournament);
        }

        @Override
        public void addTable(String tableId, Map<String, String> props) {
            Element table = _doc.createElement("table");
            table.setAttribute("action", "add");
            table.setAttribute("id", tableId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                table.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(table);
        }

        @Override
        public void updateTable(String tableId, Map<String, String> props) {
            Element table = _doc.createElement("table");
            table.setAttribute("action", "update");
            table.setAttribute("id", tableId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                table.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(table);
        }

        @Override
        public void removeTable(String tableId) {
            Element table = _doc.createElement("table");
            table.setAttribute("action", "remove");
            table.setAttribute("id", tableId);
            _hall.appendChild(table);
        }
    }
}
