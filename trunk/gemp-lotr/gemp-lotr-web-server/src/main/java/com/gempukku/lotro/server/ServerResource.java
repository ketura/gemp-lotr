package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.LoginInvalidException;
import com.gempukku.lotro.db.PlayerStatistic;
import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.game.*;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
    @Context
    private GameRecorder _gameRecorder;

    @Path("/login")
    @POST
    @Produces("text/html")
    public void login(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) throws Exception {
        Player player = _playerDao.loginUser(login, password);
        if (player != null) {
            if (player.getType().contains("u")) {
                logUser(request, login);
            } else {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
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
    public void register(
            @FormParam("login") String login,
            @FormParam("password") String password,
            @Context HttpServletRequest request) throws SQLException {
        try {
            if (_playerDao.registerUser(login, password, request.getRemoteAddr())) {
                logUser(request, login);
            } else {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }
        } catch (LoginInvalidException exp) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @Path("/replay/{replayId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public StreamingOutput getReplay(
            @PathParam("replayId") String replayId) throws ParserConfigurationException {
        if (!replayId.contains("$"))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        if (replayId.contains("."))
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        final String[] split = replayId.split("\\$");
        if (split.length != 2)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        return new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                final InputStream recordedGame = _gameRecorder.getRecordedGame(split[0], split[1]);
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
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

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

        int day = 1000 * 60 * 60 * 24;
        int week = 1000 * 60 * 60 * 24 * 7;
        sb.append("Tables count: ").append(_hallServer.getTablesCount()).append(", players in hall: ").append(_chatServer.getChatRoom("Game Hall").getUsersInRoom().size())
                .append(", games played in last 24 hours: ").append(_gameHistoryService.getGamesPlayedCount(System.currentTimeMillis() - day, day))
                .append(",<br/> active players in last week: ").append(_gameHistoryService.getActivePlayersCount(System.currentTimeMillis() - week, week));

        return sb.toString();
    }

    @Path("/stats")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getServerStats(
            @QueryParam("startDay") String startDay,
            @QueryParam("length") String length,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            long from = format.parse(startDay).getTime();
            Date to = format.parse(startDay);
            if (length.equals("month"))
                to.setMonth(to.getMonth() + 1);
            else if (length.equals("week"))
                to.setDate(to.getDate() + 7);
            else if (length.equals("day"))
                to.setDate(to.getDate() + 1);
            else
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            long duration = to.getTime() - from;

            int activePlayers = _gameHistoryService.getActivePlayersCount(from, duration);
            int gamesCount = _gameHistoryService.getGamesPlayedCount(from, duration);

            GameHistoryStatistics gameHistoryStatistics = _gameHistoryService.getGameHistoryStatistics(from, duration);

            DecimalFormat percFormat = new DecimalFormat("#0.0%");

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element stats = doc.createElement("stats");
            stats.setAttribute("activePlayers", String.valueOf(activePlayers));
            stats.setAttribute("gamesCount", String.valueOf(gamesCount));
            stats.setAttribute("start", format.format(new Date(from)));
            stats.setAttribute("end", format.format(new Date(from + duration - 1)));
            for (GameHistoryStatistics.FormatStat formatStat : gameHistoryStatistics.getFormatStats()) {
                Element formatStatElem = doc.createElement("formatStat");
                formatStatElem.setAttribute("format", formatStat.getFormat());
                formatStatElem.setAttribute("count", String.valueOf(formatStat.getCount()));
                formatStatElem.setAttribute("perc", percFormat.format(formatStat.getPercentage()));
                stats.appendChild(formatStatElem);
            }

            doc.appendChild(stats);
            return doc;
        } catch (ParseException exp) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    @Path("/playerStats")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getServerStats(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        List<PlayerStatistic> casualStatistics = _gameHistoryService.getCasualPlayerStatistics(resourceOwner);
        List<PlayerStatistic> competitiveStatistics = _gameHistoryService.getCompetitivePlayerStatistics(resourceOwner);

        DecimalFormat percFormat = new DecimalFormat("#0.0%");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element stats = doc.createElement("playerStats");

        Element casual = doc.createElement("casual");
        appendStatistics(casualStatistics, percFormat, doc, casual);
        stats.appendChild(casual);

        Element competitive = doc.createElement("competitive");
        appendStatistics(competitiveStatistics, percFormat, doc, competitive);
        stats.appendChild(competitive);

        doc.appendChild(stats);
        return doc;
    }

    private void appendStatistics(List<PlayerStatistic> statistics, DecimalFormat percFormat, Document doc, Element type) {
        for (PlayerStatistic casualStatistic : statistics) {
            Element entry = doc.createElement("entry");
            entry.setAttribute("deckName", casualStatistic.getDeckName());
            entry.setAttribute("format", casualStatistic.getFormatName());
            entry.setAttribute("wins", String.valueOf(casualStatistic.getWins()));
            entry.setAttribute("losses", String.valueOf(casualStatistic.getLosses()));
            entry.setAttribute("perc", percFormat.format(1f * casualStatistic.getWins() / (casualStatistic.getLosses() + casualStatistic.getWins())));
            type.appendChild(entry);
        }
    }

    private void logUser(HttpServletRequest request, String login) throws SQLException {
        _playerDao.updateLastLoginIp(login, request.getRemoteAddr());
        request.getSession().setAttribute("logged", login);
    }

    private void logoutUser(HttpServletRequest request) {
        request.getSession().removeAttribute("logged");
    }

    private String getUserNameIfLogged(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("logged");
    }
}