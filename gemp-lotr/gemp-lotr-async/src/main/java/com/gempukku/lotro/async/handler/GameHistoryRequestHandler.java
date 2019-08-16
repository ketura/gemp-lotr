package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.game.GameHistoryService;
import com.gempukku.lotro.game.Player;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GameHistoryRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private GameHistoryService _gameHistoryService;

    public GameHistoryRequestHandler(Map<Type, Object> context) {
        super(context);

        _gameHistoryService = extractObject(context, GameHistoryService.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.equals("") && request.getMethod() == HttpMethod.GET) {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
            String participantId = getQueryParameterSafely(queryDecoder, "participantId");
            int start = Integer.parseInt(getQueryParameterSafely(queryDecoder, "start"));
            int count = Integer.parseInt(getQueryParameterSafely(queryDecoder, "count"));

            if (start < 0 || count < 1 || count > 100)
                throw new HttpProcessingException(400);

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

            responseWriter.writeXmlResponse(doc);
        } else if (uri.equals("/list") && request.getMethod() == HttpMethod.GET) {
            final List<GameHistoryEntry> playerGameHistory = _gameHistoryService.getTrackableGames(100);

            StringBuilder sb = new StringBuilder();
            sb.append("<html><body><table>");

            sb.append("<tr>");
            sb.append("<th rowspan='2'>Start time</th><th rowspan='2'>End time</th>");
            sb.append("<th>Winner</th><th>Reason</th><th>Deck name</th><th>Replay</th>");
            sb.append("</tr>");
            sb.append("<tr>");
            sb.append("<th>Loser</th><th>Reason</th><th>Deck name</th><th>Replay</th>");
            sb.append("</tr>");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (GameHistoryEntry gameHistoryEntry : playerGameHistory) {
                String winnerLink = "http://www.gempukku.com/gemp-lotr/game.html?replayId=" + gameHistoryEntry.getWinner() + "$" + gameHistoryEntry.getWinnerRecording();
                String loserLink = "http://www.gempukku.com/gemp-lotr/game.html?replayId=" + gameHistoryEntry.getLoser() + "$" + gameHistoryEntry.getLoserRecording();

                sb.append("<tr>");
                sb.append("<td rowspan='2'>" + dateFormat.format(new Date(gameHistoryEntry.getStartTime().getTime())) + "</td><td rowspan='2'>" + dateFormat.format(new Date(gameHistoryEntry.getEndTime().getTime())) + "</td>");
                sb.append("<td>" + gameHistoryEntry.getWinner() + "</td><td>" + gameHistoryEntry.getWinReason() + "</td><td>" + gameHistoryEntry.getWinnerDeckName() + "</td><td><a target='_blank' href='" + winnerLink + "'>Replay</a></td>");
                sb.append("</tr>");
                sb.append("<tr>");
                sb.append("<td>" + gameHistoryEntry.getLoser() + "</td><td>" + gameHistoryEntry.getLoseReason() + "</td><td>" + gameHistoryEntry.getLoserDeckName() + "</td><td><a target='_blank' href='" + loserLink + "'>Replay</a></td>");
                sb.append("</tr>");
            }

            sb.append("</table></body></html>");
            responseWriter.writeHtmlResponse(sb.toString());
        } else {
            throw new HttpProcessingException(404);
        }
    }
}
