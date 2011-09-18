package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.Deck;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.hall.HallException;
import com.gempukku.lotro.hall.HallInfoVisitor;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.sun.jersey.spi.resource.Singleton;
import org.apache.commons.lang.StringEscapeUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
@Path("/")
public class ServerResource {
    private static final Logger _logger = Logger.getLogger(ServerResource.class);
    private boolean _test = true;

    private HallServer _hallServer;
    private LotroServer _lotroServer;
    private ChatServer _chatServer;

    public ServerResource() {
        _logger.debug("starting resource");

        try {
            _chatServer = new ChatServer();
            _chatServer.startServer();

            _lotroServer = new LotroServer(_chatServer);
            _lotroServer.startServer();

            _hallServer = new HallServer(_lotroServer, _chatServer, _test);
            _hallServer.startServer();
        } catch (RuntimeException exp) {
            _logger.error("Error while creating resource", exp);
            exp.printStackTrace();
        }
        _logger.debug("Resource created");
    }

    @Path("/login")
    @POST
    @Produces("text/html")
    public String login(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) throws Exception {
        if (_lotroServer.getPlayerDao().loginUser(login, password) != null) {
            logUser(request, login);
            return "<script>location.href='hall.html';</script>";
        } else {
            return "Invalid login or password. Please try again.<br/>" + getLoginHTML();
        }
    }

    @Path("/register")
    @POST
    @Produces("text/html")
    public String register(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) throws Exception {
        if (_lotroServer.getPlayerDao().registerUser(login, password)) {
            logUser(request, login);
            return "<script>location.href='hall.html';</script>";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("User with this name already exists or your login is invalid.<br/>");
            sb.append("Login must be between 2-10 characters long, contain only<br/>english letters, numbers or _ (underscore) and - (dash) characters.<br/>");
            sb.append("Try to <button id='clickToRegister'>register</button> again.");
            return sb.toString();
        }
    }

    private String getLoginHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("Login: <input id='login' type='text'><br/>");
        sb.append("Password: <input id='password' type='password'><br/>");
        sb.append("<button id='loginButton'>Login</button>");
        return sb.toString();
    }

    @Path("/game/{gameId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getGameState(
            @PathParam("gameId") String gameId,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);

        if (gameMediator == null)
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
        if (!_test)
            participantId = getLoggedUser(request);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
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
        if (!_test)
            participantId = getLoggedUser(request);

        long start = System.currentTimeMillis();

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
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
        if (time > 100)
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
        if (!_test)
            participantId = getLoggedUser(request);

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
    public Document createDeck(
            @PathParam("deckType") String deckType,
            @FormParam("participantId") String participantId,
            @FormParam("deckContents") String contents,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        Deck deck = _lotroServer.validateDeck(contents);
        if (deck == null)
            sendError(Response.Status.BAD_REQUEST);

        deckDao.setDeckForPlayer(player, deckType, deck);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("ok");
        doc.appendChild(deckElem);

        return doc;
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
        if (!_test)
            participantId = getLoggedUser(request);

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

    @Path("/chat/{room}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getMessages(
            @PathParam("room") String room,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        ChatRoomMediator chatRoom = _chatServer.getChatRoom(room);
        if (chatRoom == null)
            sendError(Response.Status.NOT_FOUND);

        List<ChatMessage> chatMessages = chatRoom.joinUser(participantId);
        Set<String> usersInRoom = chatRoom.getUsersInRoom();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        serializeChatRoomData(room, chatMessages, usersInRoom, doc);

        return doc;
    }

    @Path("/chat/{room}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document postMessage(
            @PathParam("room") String room,
            @FormParam("participantId") String participantId,
            @FormParam("message") String message,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        ChatRoomMediator chatRoom = _chatServer.getChatRoom(room);
        if (chatRoom == null)
            sendError(Response.Status.NOT_FOUND);

        if (message != null)
            chatRoom.sendMessage(participantId, StringEscapeUtils.escapeHtml(message));

        List<ChatMessage> chatMessages = chatRoom.getPendingMessages(participantId);
        Set<String> usersInRoom = chatRoom.getUsersInRoom();

        if (chatMessages == null)
            sendError(Response.Status.NOT_FOUND);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        serializeChatRoomData(room, chatMessages, usersInRoom, doc);

        return doc;
    }

    @Path("/hall")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getHall(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element hall = doc.createElement("hall");

        _hallServer.processTables(participantId, new SerializeHallInfoVisitor(doc, hall));
        for (String format : _hallServer.getSupportedFormats()) {
            Element formatElem = doc.createElement("format");
            formatElem.appendChild(doc.createTextNode(format));
            hall.appendChild(formatElem);
        }

        doc.appendChild(hall);

        return doc;
    }

    @Path("/hall/{table}")
    @POST
    public Document joinTable(
            @PathParam("table") String tableId,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        try {
            _hallServer.joinTableAsPlayer(tableId, participantId);
            return null;
        } catch (HallException e) {
            return marshalException(e);
        }
    }

    @Path("/hall")
    @POST
    public Document createTable(
            @FormParam("format") String format,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        try {
            _hallServer.createNewTable(format, participantId);
            return null;
        } catch (HallException e) {
            return marshalException(e);
        }
    }

    @Path("/hall/leave")
    @POST
    public void leaveTable(
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        _hallServer.leaveAwaitingTables(participantId);
    }

    @GET
    @Produces("text/html")
    public String getStatus(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        StringBuilder sb = new StringBuilder();
        participantId = getUserNameIfLogged(request);
        if (participantId != null) {
            sb.append("<script>location.href='hall.html';</script>");
        } else {
            sb.append("You are not logged in, log in below or <button id='clickToRegister'>register</button>.");
            sb.append("<div class='status'>There are currently ").append(_hallServer.getTablesCount()).append(" tables in the Game Hall</div>");
            sb.append(getLoginHTML());
        }

        return sb.toString();
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

    private class SerializeHallInfoVisitor implements HallInfoVisitor {
        private Document _doc;
        private Element _hall;

        public SerializeHallInfoVisitor(Document doc, Element hall) {
            _doc = doc;
            _hall = hall;
        }

        @Override
        public void playerIsWaiting(boolean waiting) {
            _hall.setAttribute("waiting", String.valueOf(waiting));
        }

        @Override
        public void visitTable(String tableId, String tableStatus, Set<String> playerIds) {
            Element table = _doc.createElement("table");
            table.setAttribute("id", tableId);
            table.setAttribute("status", tableStatus);
            table.setAttribute("players", mergeStrings(playerIds));
            _hall.appendChild(table);
        }

        @Override
        public void runningPlayerGame(String gameId) {
            Element runningGame = _doc.createElement("game");
            runningGame.setAttribute("id", gameId);
            _hall.appendChild(runningGame);
        }
    }

    private String mergeStrings(Set<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings)
            sb.append(string).append(",");
        if (sb.length() > 0)
            return sb.deleteCharAt(sb.length() - 1).toString();
        return sb.toString();
    }

    private void serializeChatRoomData(String room, List<ChatMessage> chatMessages, Set<String> usersInRoom, Document doc) {
        Element chatElem = doc.createElement("chat");
        chatElem.setAttribute("roomName", room);
        doc.appendChild(chatElem);

        for (ChatMessage chatMessage : chatMessages) {
            Element message = doc.createElement("message");
            message.setAttribute("from", chatMessage.getFrom());
            message.setAttribute("date", String.valueOf(chatMessage.getWhen().getTime()));
            message.appendChild(doc.createTextNode(chatMessage.getMessage()));
            chatElem.appendChild(message);
        }

        for (String userInRoom : usersInRoom) {
            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(userInRoom));
            chatElem.appendChild(user);
        }
    }

    private class SerializationVisitor implements ParticipantCommunicationVisitor {
        private Document _doc;
        private Element _element;

        private SerializationVisitor(Document doc, Element element) {
            _doc = doc;
            _element = element;
        }

        @Override
        public void visitGameEvent(GameEvent gameEvent) {
            _element.appendChild(serializeEvent(_doc, gameEvent));
        }

        @Override
        public void visitAwaitingDecision(AwaitingDecision awaitingDecision) {
            _element.appendChild(serializeDecision(_doc, awaitingDecision));
        }

        @Override
        public void visitClock(Map<String, Integer> secondsLeft) {
            _element.appendChild(serializeClocks(_doc, secondsLeft));
        }

        @Override
        public void visitSkirmishStats(int fpStrength, int shadowStrength) {
            Element skirmish = _doc.createElement("skirmish");
            skirmish.setAttribute("fpStrength", String.valueOf(fpStrength));
            skirmish.setAttribute("shadowStrength", String.valueOf(shadowStrength));
            _element.appendChild(skirmish);
        }
    }

    private Node serializeClocks(Document doc, Map<String, Integer> secondsLeft) {
        Element clocks = doc.createElement("clocks");
        for (Map.Entry<String, Integer> userClock : secondsLeft.entrySet()) {
            Element clock = doc.createElement("clock");
            clock.setAttribute("participantId", userClock.getKey());
            clock.appendChild(doc.createTextNode(userClock.getValue().toString()));
            clocks.appendChild(clock);
        }

        return clocks;
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
        if (gameEvent.getMessage() != null)
            eventElem.setAttribute("message", gameEvent.getMessage());

        return eventElem;
    }

    private void logUser(HttpServletRequest request, String login) {
        request.getSession().setAttribute("logged", login);
    }

    private String getUserNameIfLogged(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("logged");
    }

    private String getLoggedUser(HttpServletRequest request) {
        String loggedUser = (String) request.getSession().getAttribute("logged");
        if (loggedUser == null)
            sendError(Response.Status.UNAUTHORIZED);
        return loggedUser;
    }

    private void sendError(Response.Status status) {
        WebApplicationException webApplicationException = new WebApplicationException(status);
//        _logger.debug("Sending error to user: " + status.getStatusCode(), webApplicationException);
        throw webApplicationException;
    }
}
