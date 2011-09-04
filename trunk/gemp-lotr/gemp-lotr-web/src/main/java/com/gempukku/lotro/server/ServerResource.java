package com.gempukku.lotro.server;

import com.gempukku.lotro.GameEvent;
import com.gempukku.lotro.LotroGameMediator;
import com.gempukku.lotro.LotroGameParticipant;
import com.gempukku.lotro.LotroServer;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.Deck;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultLotroFormat;
import com.gempukku.lotro.game.ParticipantCommunicationVisitor;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.sun.jersey.spi.resource.Singleton;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;

@Singleton
@Path("/")
public class ServerResource {
    private static final Logger _logger = Logger.getLogger(ServerResource.class);

    private LotroServer _lotroServer;

    public ServerResource() {
        _logger.debug("starting resource");

        try {
            _lotroServer = new LotroServer();
            _lotroServer.startServer();
        } catch (RuntimeException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        }
        _logger.debug("Resource created");
    }

    private void createGame(String gameId) {
        LotroGameParticipant[] participants = new LotroGameParticipant[2];
        participants[0] = new LotroGameParticipant("MarcinS1", _lotroServer.getParticipantDeck("MarcinS1"));
        participants[1] = new LotroGameParticipant("MarcinS2", _lotroServer.getParticipantDeck("MarcinS2"));

        _lotroServer.createNewGame(new DefaultLotroFormat(true), participants, gameId);
        _lotroServer.getGameById(gameId).startGame();
    }

    @Path("/login")
    @POST
    public void login(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) {
        _logger.debug("/server/login " + login + ", " + password);
        if (login == null)
            sendError(Response.Status.NOT_FOUND);
        if (password != null && password.equals("test"))
            logUser(request, login);
    }

    @Path("/game/{gameId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getGameState(
            @PathParam("gameId") String gameId,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
//        String participantId = getLoggedUser(request);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            createGame(gameId);
        gameMediator = _lotroServer.getGameById(gameId);

        if (gameMediator == null)
            sendError(Response.Status.NOT_FOUND);

        if (participantId == null)
            sendError(Response.Status.NOT_FOUND);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element gameState = doc.createElement("gameState");

        gameMediator.singupUserForGame(participantId, new SerializationVisitor(doc, gameState));

        doc.appendChild(gameState);
        return doc;
    }

    @Path("/game/{gameId}/cardInfo")
    @GET
    @Produces("text/html")
    public String getCardInfo(
            @PathParam("gameId") String gameId,
            @QueryParam("cardId") int cardId,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
//        String participantId = getLoggedUser(request);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            sendError(Response.Status.NOT_FOUND);

        if (participantId == null)
            sendError(Response.Status.NOT_FOUND);

        return gameMediator.produceCardInfo(participantId, cardId);
    }

    @Path("/game/{gameId}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document gameEvent(
            @PathParam("gameId") String gameId,
            @FormParam("participantId") String participantId,
            @FormParam("decisionId") Integer decisionId,
            @FormParam("decisionValue") String decisionValue,
            @Context HttpServletRequest request) throws ParserConfigurationException {
//        String participantId = getLoggedUser(request);

        long start = System.currentTimeMillis();

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            sendError(Response.Status.NOT_FOUND);

        if (participantId == null)
            sendError(Response.Status.NOT_FOUND);

        if (decisionId != null)
            gameMediator.playerAnswered(participantId, decisionId, decisionValue);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element update = doc.createElement("update");

        gameMediator.processCommunicationChannel(participantId, new SerializationVisitor(doc, update));

        doc.appendChild(update);

        long time = System.currentTimeMillis() - start;
        if (time > 10)
            _logger.debug("Processing time: " + time);

        return doc;
    }

    @Path("/deck/{deckType}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getDeck(
            @PathParam("deckType") String deckType,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Deck deck = deckDao.getDeckForPlayer(player, deckType);
        if (deck == null) {
            Document doc = documentBuilder.newDocument();
            Element deckElem = doc.createElement("deck");
            doc.appendChild(deckElem);
            return doc;
        }

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("deck");
        doc.appendChild(deckElem);

        Element ringBearer = doc.createElement("ringBearer");
        ringBearer.setAttribute("blueprintId", deck.getRingBearer());
        deckElem.appendChild(ringBearer);

        Element ring = doc.createElement("ring");
        ring.setAttribute("blueprintId", deck.getRing());
        deckElem.appendChild(ring);

        for (String s : deck.getSites()) {
            Element site = doc.createElement("site");
            site.setAttribute("blueprintId", s);
            deckElem.appendChild(site);
        }
        for (String s : deck.getDeck()) {
            Element card = doc.createElement("card");
            card.setAttribute("blueprintId", s);
            deckElem.appendChild(card);
        }

        return doc;
    }

    @Path("/deck/{deckType}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public void createDeck(
            @PathParam("deckType") String deckType,
            @FormParam("participantId") String participantId,
            @FormParam("deckContents") String contents) {

        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        Deck deck = _lotroServer.validateDeck(contents);
        if (deck == null)
            sendError(Response.Status.BAD_REQUEST);

        deckDao.setDeckForPlayer(player, deckType, deck);
    }

    @Path("/collection/{collectionType}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getCollection(
            @PathParam("collectionType") String collectionType,
            @QueryParam("participantId") String participantId,
            @QueryParam("filter") String filter,
            @QueryParam("start") int start,
            @QueryParam("count") int count,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (collectionType == null || !collectionType.equals("default"))
            sendError(Response.Status.NOT_FOUND);

        CardCollection collection = _lotroServer.getDefaultCollection();
        Map<String, Integer> filteredResult = collection.filter(filter);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionElem = doc.createElement("collection");
        collectionElem.setAttribute("count", String.valueOf(filteredResult.size()));
        doc.appendChild(collectionElem);

        int index = 0;
        for (Map.Entry<String, Integer> stringIntegerEntry : filteredResult.entrySet()) {
            if (index >= start && index < start + count) {
                Element card = doc.createElement("card");
                card.setAttribute("count", stringIntegerEntry.getValue().toString());
                card.setAttribute("blueprintId", stringIntegerEntry.getKey());
                collectionElem.appendChild(card);
            }
            index++;
        }

        return doc;
    }

    private class SerializationVisitor implements ParticipantCommunicationVisitor {
        private Document _doc;
        private Element _element;

        private SerializationVisitor(Document doc, Element element) {
            _doc = doc;
            _element = element;
        }

        @Override
        public void visitWarning(String warning) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void visitGameEvent(GameEvent gameEvent) {
            _element.appendChild(serializeEvent(_doc, gameEvent));
        }

        @Override
        public void visitAwaitingDecision(AwaitingDecision awaitingDecision) {
            _element.appendChild(serializeDecision(_doc, awaitingDecision));
        }
    }

    private Node serializeDecision(Document doc, AwaitingDecision decision) {
        Element decisionElem = doc.createElement("decision");
        decisionElem.setAttribute("id", String.valueOf(decision.getAwaitingDecisionId()));
        decisionElem.setAttribute("type", decision.getDecisionType().name());
        if (decision.getText() != null)
            decisionElem.setAttribute("text", decision.getText());
        for (Map.Entry<String, Object> paramEntry : decision.getDecisionParameters().entrySet()) {
            if (paramEntry.getValue() instanceof String) {
                Element decisionParam = doc.createElement("parameter");
                decisionParam.setAttribute("name", paramEntry.getKey());
                decisionParam.setAttribute("value", (String) paramEntry.getValue());
                decisionElem.appendChild(decisionParam);
            } else if (paramEntry.getValue() instanceof String[]) {
                for (String value : (String[]) paramEntry.getValue()) {
                    Element decisionParam = doc.createElement("parameter");
                    decisionParam.setAttribute("name", paramEntry.getKey());
                    decisionParam.setAttribute("value", value);
                    decisionElem.appendChild(decisionParam);
                }
            }
        }
        return decisionElem;
    }

    private Node serializeEvent(Document doc, GameEvent gameEvent) {
        Element eventElem = doc.createElement("gameEvent");
        eventElem.setAttribute("type", gameEvent.getType().name());
        if (gameEvent.getBlueprintId() != null)
            eventElem.setAttribute("blueprintId", gameEvent.getBlueprintId());
        if (gameEvent.getCardId() != null)
            eventElem.setAttribute("cardId", gameEvent.getCardId().toString());
        if (gameEvent.getIndex() != null)
            eventElem.setAttribute("index", gameEvent.getIndex().toString());
        if (gameEvent.getParticipantId() != null)
            eventElem.setAttribute("participantId", gameEvent.getParticipantId());
        if (gameEvent.getAllParticipantIds() != null) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String participantId : gameEvent.getAllParticipantIds()) {
                if (!first) sb.append(",");
                sb.append(participantId);
                first = false;
            }
            eventElem.setAttribute("allParticipantIds", sb.toString());
        }
        if (gameEvent.getPhase() != null)
            eventElem.setAttribute("phase", gameEvent.getPhase().name());
        if (gameEvent.getTargetCardId() != null)
            eventElem.setAttribute("targetCardId", gameEvent.getTargetCardId().toString());
        if (gameEvent.getZone() != null)
            eventElem.setAttribute("zone", gameEvent.getZone().name());
        if (gameEvent.getToken() != null)
            eventElem.setAttribute("token", gameEvent.getToken().name());
        if (gameEvent.getCount() != null)
            eventElem.setAttribute("count", gameEvent.getCount().toString());
        if (gameEvent.getOpposingCardIds() != null) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (int cardId : gameEvent.getOpposingCardIds()) {
                if (!first) sb.append(",");
                sb.append(cardId);
                first = false;
            }
            eventElem.setAttribute("opposingCardIds", sb.toString());
        }

        return eventElem;
    }

    private void logUser(HttpServletRequest request, String login) {
        request.getSession().setAttribute("logged", login);
    }

    private String getLoggedUser(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("logged");
    }

    private void sendError(Response.Status status) {
        throw new WebApplicationException(status);
    }
}
