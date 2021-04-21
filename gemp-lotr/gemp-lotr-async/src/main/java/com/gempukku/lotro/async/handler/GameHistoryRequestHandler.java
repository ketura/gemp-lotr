package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.db.vo.GameHistoryEntry;
import com.gempukku.lotro.game.GameHistoryService;
import com.gempukku.lotro.game.Player;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
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
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
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
                historyEntry.setAttribute("winner", gameHistoryEntry.winner);
                historyEntry.setAttribute("loser", gameHistoryEntry.loser);

                historyEntry.setAttribute("winReason", gameHistoryEntry.win_reason);
                historyEntry.setAttribute("loseReason", gameHistoryEntry.lose_reason);

                historyEntry.setAttribute("formatName", gameHistoryEntry.format_name);
                String tournament = gameHistoryEntry.tournament;
                if (tournament != null)
                    historyEntry.setAttribute("tournament", tournament);

                if (gameHistoryEntry.winner.equals(resourceOwner.getName()) && gameHistoryEntry.win_recording_id != null) {
                    historyEntry.setAttribute("gameRecordingId", gameHistoryEntry.win_recording_id);
                    historyEntry.setAttribute("deckName", gameHistoryEntry.winner_deck_name);
                } else if (gameHistoryEntry.loser.equals(resourceOwner.getName()) && gameHistoryEntry.lose_recording_id != null) {
                    historyEntry.setAttribute("gameRecordingId", gameHistoryEntry.lose_recording_id);
                    historyEntry.setAttribute("deckName", gameHistoryEntry.loser_deck_name);
                }

                historyEntry.setAttribute("startTime", String.valueOf(gameHistoryEntry.GetStartDate().getTime()));
                historyEntry.setAttribute("endTime", String.valueOf(gameHistoryEntry.GetEndDate().getTime()));

                gameHistory.appendChild(historyEntry);
            }

            doc.appendChild(gameHistory);

            responseWriter.writeXmlResponse(doc);
        } else if (uri.equals("/list") && request.getMethod() == HttpMethod.GET) {
            final List<GameHistoryEntry> playerGameHistory = _gameHistoryService.getTrackableGames(100);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element gameHistory = doc.createElement("gameHistory");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (GameHistoryEntry gameHistoryEntry : playerGameHistory) {
                Element historyEntry = doc.createElement("historyEntry");
                historyEntry.setAttribute("winner", gameHistoryEntry.winner);
                historyEntry.setAttribute("loser", gameHistoryEntry.loser);

                historyEntry.setAttribute("winReason", gameHistoryEntry.win_reason);
                historyEntry.setAttribute("loseReason", gameHistoryEntry.lose_reason);

                historyEntry.setAttribute("formatName", gameHistoryEntry.format_name);

                historyEntry.setAttribute("winnerRecordingLink", "http://www.gempukku.com/gemp-lotr/game.html?replayId="+gameHistoryEntry.winner+"$"+gameHistoryEntry.win_recording_id);
                historyEntry.setAttribute("winnerDeckName", gameHistoryEntry.winner_deck_name);

                historyEntry.setAttribute("loserRecordingLink", "http://www.gempukku.com/gemp-lotr/game.html?replayId="+gameHistoryEntry.loser+"$"+gameHistoryEntry.lose_recording_id);
                historyEntry.setAttribute("loserDeckName", gameHistoryEntry.loser_deck_name);

                historyEntry.setAttribute("startTime", dateFormat.format(new Date(gameHistoryEntry.GetStartDate().getTime())));
                historyEntry.setAttribute("endTime", dateFormat.format(new Date(gameHistoryEntry.GetEndDate().getTime())));

                gameHistory.appendChild(historyEntry);
            }

            doc.appendChild(gameHistory);

            responseWriter.writeXmlResponse(doc);
        } else if (uri.equals("/list/html") && request.getMethod() == HttpMethod.GET) {
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
                String winnerLink = "http://www.gempukku.com/gemp-lotr/game.html?replayId=" + gameHistoryEntry.winner + "$" + gameHistoryEntry.win_recording_id;
                String loserLink = "http://www.gempukku.com/gemp-lotr/game.html?replayId=" + gameHistoryEntry.loser + "$" + gameHistoryEntry.lose_recording_id;

                sb.append("<tr>");
                sb.append("<td rowspan='2'>" + dateFormat.format(new Date(gameHistoryEntry.GetStartDate().getTime())) + "</td><td rowspan='2'>" + dateFormat.format(new Date(gameHistoryEntry.GetEndDate().getTime())) + "</td>");
                sb.append("<td>" + gameHistoryEntry.winner + "</td><td>" + gameHistoryEntry.win_reason + "</td><td>" + gameHistoryEntry.winner_deck_name + "</td><td><a target='_blank' href='" + winnerLink + "'>Replay</a></td>");
                sb.append("</tr>");
                sb.append("<tr>");
                sb.append("<td>" + gameHistoryEntry.loser + "</td><td>" + gameHistoryEntry.lose_reason + "</td><td>" + gameHistoryEntry.lose_reason + "</td><td><a target='_blank' href='" + loserLink + "'>Replay</a></td>");
                sb.append("</tr>");
            }

            sb.append("</table></body></html>");
            responseWriter.writeHtmlResponse(sb.toString());
        } else {
            throw new HttpProcessingException(404);
        }
    }
}
