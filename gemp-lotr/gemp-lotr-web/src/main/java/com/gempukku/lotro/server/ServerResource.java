package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.state.EventSerializer;
import com.gempukku.lotro.game.state.GameEvent;
import com.gempukku.lotro.hall.HallException;
import com.gempukku.lotro.hall.HallInfoVisitor;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.modifiers.LoggingThreadLocal;
import com.gempukku.lotro.logic.vo.LotroDeck;
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
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Singleton
@Path("/")
public class ServerResource {
    private static final Logger _logger = Logger.getLogger(ServerResource.class);
    private boolean _test = System.getProperty("test") != null;

    private LotroCardBlueprintLibrary _library;

    private HallServer _hallServer;
    private LotroServer _lotroServer;
    private ChatServer _chatServer;
    private LeagueService _leagueService;
    private CollectionDAO _collectionDao;

    public ServerResource() {
        _logger.debug("starting resource");

        try {
            DbAccess dbAccess = new DbAccess();
            _library = new LotroCardBlueprintLibrary();

            _chatServer = new ChatServer();
            _chatServer.startServer();

            _lotroServer = new LotroServer(dbAccess, _library, _chatServer, _test);
            _lotroServer.startServer();

            _collectionDao = new CollectionDAO(dbAccess, _library, _lotroServer.getDefaultCollection());

            _leagueService = new LeagueService(dbAccess, _collectionDao, _library);

            _hallServer = new HallServer(_lotroServer, _chatServer, _leagueService, _collectionDao, _test);
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

    @Path("/replay/{replayId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public StreamingOutput getReplay(
            @PathParam("replayId") String replayId) throws ParserConfigurationException {
        if (!replayId.contains("$"))
            sendError(Response.Status.NOT_FOUND);
        if (replayId.contains("."))
            sendError(Response.Status.NOT_FOUND);

        final String[] split = replayId.split("\\$");
        if (split.length != 2)
            sendError(Response.Status.NOT_FOUND);

        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                final InputStream recordedGame = _lotroServer.getGameRecording(split[0], split[1]);
                if (recordedGame == null)
                    sendError(Response.Status.NOT_FOUND);
                try {
                    byte[] bytes = new byte[1024];
                    int count;
                    while ((count = recordedGame.read(bytes)) != -1)
                        outputStream.write(bytes, 0, count);
                } finally {
                    recordedGame.close();
                }
            }
        };
    }

    @Path("/gameHistory")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getGameHistory(
            @QueryParam("start") int start,
            @QueryParam("count") int count,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        if (start < 0 || count < 1 || count > 100)
            sendError(Response.Status.BAD_REQUEST);

        PlayerDAO playerDao = _lotroServer.getPlayerDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        final List<GameHistoryEntry> playerGameHistory = _lotroServer.getPlayerGameHistory(player, start, count);
        int recordCount = _lotroServer.getPlayerGameHistoryRecordCount(player);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element gameHistory = doc.createElement("gameHistory");
        gameHistory.setAttribute("count", String.valueOf(recordCount));
        gameHistory.setAttribute("playerId", participantId);

        for (GameHistoryEntry gameHistoryEntry : playerGameHistory) {
            Element historyEntry = doc.createElement("historyEntry");
            historyEntry.setAttribute("winner", gameHistoryEntry.getWinner());
            historyEntry.setAttribute("loser", gameHistoryEntry.getLoser());

            historyEntry.setAttribute("winReason", gameHistoryEntry.getWinReason());
            historyEntry.setAttribute("loseReason", gameHistoryEntry.getLoseReason());

            if (gameHistoryEntry.getWinner().equals(participantId) && gameHistoryEntry.getWinnerRecording() != null)
                historyEntry.setAttribute("gameRecordingId", gameHistoryEntry.getWinnerRecording());
            else if (gameHistoryEntry.getLoser().equals(participantId) && gameHistoryEntry.getLoserRecording() != null)
                historyEntry.setAttribute("gameRecordingId", gameHistoryEntry.getLoserRecording());

            historyEntry.setAttribute("startTime", String.valueOf(gameHistoryEntry.getStartTime().getTime()));
            historyEntry.setAttribute("endTime", String.valueOf(gameHistoryEntry.getEndTime().getTime()));

            gameHistory.appendChild(historyEntry);
        }

        doc.appendChild(gameHistory);
        return doc;
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

    @Path("/game/{gameId}/concede")
    @POST
    public void concede(
            @PathParam("gameId") String gameId,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            sendError(Response.Status.NOT_FOUND);

        gameMediator.concede(participantId);
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

        LoggingThreadLocal.start();
        try {
            LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
            if (gameMediator == null)
                sendError(Response.Status.NOT_FOUND);

            if (decisionId != null) {
                try {
                    gameMediator.playerAnswered(participantId, decisionId, decisionValue);
                } catch (RuntimeException exp) {
                    _logger.error("Error while sending decision", exp);
                    throw exp;
                }
            }

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element update = doc.createElement("update");

            gameMediator.processCommunicationChannel(participantId, new SerializationVisitor(doc, update));

            doc.appendChild(update);

            return doc;
        } finally {
            LoggingThreadLocal.stop(decisionId != null);
        }
    }

    @Path("/deck/list")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document listDecks(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        List<String> names = new ArrayList<String>(deckDao.getPlayerDeckNames(player));
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
        return doc;
    }

    @Path("/deck")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getDeck(
            @QueryParam("deckName") String deckName,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        LotroDeck deck = deckDao.getDeckForPlayer(player, deckName);
        if (deck == null) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element deckElem = doc.createElement("deck");
            doc.appendChild(deckElem);
            return doc;
        }

        return serializeDeck(deck);
    }

    @Path("/deck")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document saveDeck(
            @FormParam("deckName") String deckName,
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

        LotroDeck deck = _lotroServer.validateDeck(contents);
        if (deck == null)
            sendError(Response.Status.BAD_REQUEST);

        deckDao.saveDeckForPlayer(player, deckName, deck);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("ok");
        doc.appendChild(deckElem);

        return doc;
    }

    @Path("/deck/rename")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document renameDeck(
            @FormParam("oldDeckName") String oldDeckName,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        LotroDeck deck = deckDao.renameDeck(player, oldDeckName, deckName);
        if (deck == null)
            sendError(Response.Status.NOT_FOUND);

        return serializeDeck(deck);
    }

    @Path("/deck/delete")
    @POST
    public void deleteDeck(
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        PlayerDAO playerDao = _lotroServer.getPlayerDao();
        DeckDAO deckDao = _lotroServer.getDeckDao();

        Player player = playerDao.getPlayer(participantId);
        if (player == null)
            sendError(Response.Status.UNAUTHORIZED);

        deckDao.deleteDeckForPlayer(player, deckName);
    }

    private Document serializeDeck(LotroDeck deck) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

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
        for (String s : deck.getAdventureCards()) {
            Element card = doc.createElement("card");
            card.setAttribute("side", _library.getLotroCardBlueprint(s).getSide().toString());
            card.setAttribute("blueprintId", s);
            deckElem.appendChild(card);
        }

        return doc;
    }

    @Path("/deck/stats")
    @POST
    @Produces("text/html")
    public String getDeckStats(
            @FormParam("participantId") String participantId,
            @FormParam("deckContents") String contents,
            @Context HttpServletRequest request) {
        if (!_test)
            participantId = getLoggedUser(request);

        LotroDeck deck = _lotroServer.validateDeck(contents);
        if (deck == null)
            sendError(Response.Status.BAD_REQUEST);

        int fpCount = 0;
        int shadowCount = 0;
        LotroCardBlueprintLibrary library = _lotroServer.getLotroCardBlueprintLibrary();
        for (String card : deck.getAdventureCards()) {
            Side side = library.getLotroCardBlueprint(card).getSide();
            if (side == Side.SHADOW)
                shadowCount++;
            else if (side == Side.FREE_PEOPLE)
                fpCount++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<b>Free People</b>: " + fpCount + ", <b>Shadow</b>: " + shadowCount + "<br/>");

        for (Map.Entry<String, String> supportedFormats : _hallServer.getSupportedFormatNames().entrySet()) {
            String formatName = supportedFormats.getValue();
            LotroFormat format = _hallServer.getSupportedFormat(supportedFormats.getKey());
            try {
                format.validateDeck(deck);
                sb.append("<b>" + formatName + "</b>: <font color='green'>valid</font><br/>");
            } catch (DeckInvalidException exp) {
                sb.append("<b>" + formatName + "</b>: <font color='red'>" + exp.getMessage() + "</font><br/>");
            }
        }

        return sb.toString();
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


        Player player = _lotroServer.getPlayerDao().getPlayer(participantId);

        CardCollection collection = _collectionDao.getCollectionForPlayer(player, collectionType);
        if (collection == null)
            sendError(Response.Status.NOT_FOUND);

        List<CardCollection.Item> filteredResult = collection.getItems(filter);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionElem = doc.createElement("collection");
        collectionElem.setAttribute("count", String.valueOf(filteredResult.size()));
        doc.appendChild(collectionElem);

        int index = 0;
        for (CardCollection.Item item : filteredResult) {
            if (index >= start && index < start + count) {
                String blueprintId = item.getBlueprintId();
                if (item.getType() == CardCollection.Item.Type.CARD) {
                    Element card = doc.createElement("card");
                    card.setAttribute("count", String.valueOf(item.getCount()));
                    card.setAttribute("blueprintId", blueprintId);
                    Side side = item.getCardBlueprint().getSide();
                    if (side != null)
                        card.setAttribute("side", side.toString());
                    collectionElem.appendChild(card);
                } else {
                    Element pack = doc.createElement("pack");
                    pack.setAttribute("count", String.valueOf(item.getCount()));
                    pack.setAttribute("blueprintId", blueprintId);
                    collectionElem.appendChild(pack);
                }
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

        if (message != null && message.trim().length() > 0)
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
        for (Map.Entry<String, String> format : _hallServer.getSupportedFormatNames().entrySet()) {
            Element formatElem = doc.createElement("format");
            formatElem.setAttribute("type", format.getKey());
            formatElem.appendChild(doc.createTextNode(format.getValue()));
            hall.appendChild(formatElem);
        }
        for (League league : _hallServer.getRunningLeagues()) {
            Element formatElem = doc.createElement("format");
            formatElem.appendChild(doc.createTextNode(league.getName()));
            hall.appendChild(formatElem);
        }


        doc.appendChild(hall);

        return doc;
    }

    @Path("/hall/{table}")
    @POST
    public Document joinTable(
            @PathParam("table") String tableId,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        try {
            _hallServer.joinTableAsPlayer(tableId, participantId, deckName);
            return null;
        } catch (HallException e) {
            return marshalException(e);
        }
    }

    @Path("/hall")
    @POST
    public Document createTable(
            @FormParam("format") String format,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        if (!_test)
            participantId = getLoggedUser(request);

        try {
            _hallServer.createNewTable(format, participantId, deckName);
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
            sb.append("<div class='status'>Tables count: ").append(_hallServer.getTablesCount()).append(", players in hall: ").append(_chatServer.getChatRoom("Game Hall").getUsersInRoom().size())
                    .append(", games played in last 24 hours: ").append(_lotroServer.getGamesPlayedCountInLastMs(1000 * 60 * 60 * 24))
                    .append("</div>");
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
        public void visitTable(String tableId, String gameId, String tableStatus, String formatName, Set<String> playerIds, String winner) {
            Element table = _doc.createElement("table");
            table.setAttribute("id", tableId);
            if (gameId != null)
                table.setAttribute("gameId", gameId);
            table.setAttribute("status", tableStatus);
            table.setAttribute("format", formatName);
            table.setAttribute("players", mergeStrings(playerIds));
            if (winner != null)
                table.setAttribute("winner", winner);
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
        private EventSerializer _eventSerializer = new EventSerializer();

        private SerializationVisitor(Document doc, Element element) {
            _doc = doc;
            _element = element;
        }

        @Override
        public void visitGameEvent(GameEvent gameEvent) {
            _element.appendChild(_eventSerializer.serializeEvent(_doc, gameEvent));
        }

        @Override
        public void visitClock(Map<String, Integer> secondsLeft) {
            _element.appendChild(serializeClocks(_doc, secondsLeft));
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

    private void sendError(Response.Status status) throws WebApplicationException {
        throw new WebApplicationException(status);
    }
}
