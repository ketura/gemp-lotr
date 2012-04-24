package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.common.ApplicationRoot;
import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.game.GameHistoryService;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.hall.HallServer;
import com.sun.jersey.spi.resource.Singleton;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Singleton
@Path("/")
public class ServerResource extends AbstractResource {
    private static final Logger _logger = Logger.getLogger(ServerResource.class);

    @Context
    private LotroCardBlueprintLibrary _library;

    @Context
    private HallServer _hallServer;
    @Context
    private LotroServer _lotroServer;
    @Context
    private ChatServer _chatServer;
    @Context
    private GameHistoryService _gameHistoryService;

    public ServerResource() {
        if (!_test)
            ApplicationRoot.setRoot(new File("/etc/gemp-lotr"));
        else
            ApplicationRoot.setRoot(new File("i:\\gemp-lotr"));
    }

    @Path("/login")
    @POST
    @Produces("text/html")
    public String login(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) throws Exception {
        if (_playerDao.loginUser(login, password) != null) {
            logUser(request, login);
            return "<script>location.href='hall.html';</script>";
        } else {
            return "Invalid login or password. Please try again.<br/>" + getLoginHTML();
        }
    }

    @Path("/logout")
    @POST
    public void logout(
            @Context HttpServletRequest request) throws Exception {
        logoutUser(request);
    }

    @Path("/register")
    @POST
    @Produces("text/html")
    public String register(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) throws Exception {
        if (_playerDao.registerUser(login, password)) {
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
                    throw new WebApplicationException(Response.Status.NOT_FOUND);
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
        if (start < 0 || count < 1 || count > 100)
            sendError(Response.Status.BAD_REQUEST);

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        final List<GameHistoryEntry> playerGameHistory = _gameHistoryService.getGameHistoryForPlayer(resourceOwner, start, count);
        int recordCount = _gameHistoryService.getGameHistoryForPlayerCount(resourceOwner);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element gameHistory = doc.createElement("gameHistory");
        gameHistory.setAttribute("count", String.valueOf(recordCount));
        gameHistory.setAttribute("playerId", resourceOwner.getName());

        for (GameHistoryEntry gameHistoryEntry : playerGameHistory) {
            Element historyEntry = doc.createElement("historyEntry");
            historyEntry.setAttribute("winner", gameHistoryEntry.getWinner());
            historyEntry.setAttribute("loser", gameHistoryEntry.getLoser());

            historyEntry.setAttribute("winReason", gameHistoryEntry.getWinReason());
            historyEntry.setAttribute("loseReason", gameHistoryEntry.getLoseReason());

            historyEntry.setAttribute("formatName", gameHistoryEntry.getFormatName());
            String tournament = gameHistoryEntry.getTournament();
            if (tournament != null)
                historyEntry.setAttribute("tournament", tournament);

            if (gameHistoryEntry.getWinner().equals(resourceOwner.getName()) && gameHistoryEntry.getWinnerRecording() != null) {
                historyEntry.setAttribute("gameRecordingId", gameHistoryEntry.getWinnerRecording());
                historyEntry.setAttribute("deckName", gameHistoryEntry.getWinnerDeckName());
            } else if (gameHistoryEntry.getLoser().equals(resourceOwner.getName()) && gameHistoryEntry.getLoserRecording() != null) {
                historyEntry.setAttribute("gameRecordingId", gameHistoryEntry.getLoserRecording());
                historyEntry.setAttribute("deckName", gameHistoryEntry.getLoserDeckName());
            }

            historyEntry.setAttribute("startTime", String.valueOf(gameHistoryEntry.getStartTime().getTime()));
            historyEntry.setAttribute("endTime", String.valueOf(gameHistoryEntry.getEndTime().getTime()));

            gameHistory.appendChild(historyEntry);
        }

        doc.appendChild(gameHistory);
        return doc;
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
                    .append(", games played in last 24 hours: ").append(_gameHistoryService.getGamesPlayedCountInLastMs(1000 * 60 * 60 * 24))
                    .append(",<br/> active players in last week: ").append(_gameHistoryService.getActivePlayersInLastMs(1000 * 60 * 60 * 24 * 7))
                    .append("</div>");
            sb.append(getLoginHTML());
        }

        return sb.toString();
    }

    private void logUser(HttpServletRequest request, String login) {
        request.getSession().setAttribute("logged", login);
    }

    private void logoutUser(HttpServletRequest request) {
        request.getSession().removeAttribute("logged");
    }

    private String getUserNameIfLogged(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("logged");
    }
}
