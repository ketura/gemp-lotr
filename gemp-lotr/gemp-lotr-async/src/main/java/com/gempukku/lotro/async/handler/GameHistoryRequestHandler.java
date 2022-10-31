package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.common.DBDefs;
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
    private final GameHistoryService _gameHistoryService;

    public GameHistoryRequestHandler(Map<Type, Object> context) {
        super(context);

        _gameHistoryService = extractObject(context, GameHistoryService.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.equals("") && request.method() == HttpMethod.GET) {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
            String participantId = getQueryParameterSafely(queryDecoder, "participantId");
            int start = Integer.parseInt(getQueryParameterSafely(queryDecoder, "start"));
            int count = Integer.parseInt(getQueryParameterSafely(queryDecoder, "count"));

            if (start < 0 || count < 1 || count > 100)
                throw new HttpProcessingException(400);

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            final List<DBDefs.GameHistory> playerGameHistory = _gameHistoryService.getGameHistoryForPlayer(resourceOwner, start, count);
            int recordCount = _gameHistoryService.getGameHistoryForPlayerCount(resourceOwner);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element gameHistory = doc.createElement("gameHistory");
            gameHistory.setAttribute("count", String.valueOf(recordCount));
            gameHistory.setAttribute("playerId", resourceOwner.getName());

            for (DBDefs.GameHistory game : playerGameHistory) {
                Element historyEntry = doc.createElement("historyEntry");
                historyEntry.setAttribute("winner", game.winner);
                historyEntry.setAttribute("loser", game.loser);

                historyEntry.setAttribute("winReason", game.win_reason);
                historyEntry.setAttribute("loseReason", game.lose_reason);

                historyEntry.setAttribute("formatName", game.format_name);
                String tournament = game.tournament;
                if (tournament != null)
                    historyEntry.setAttribute("tournament", tournament);

                if (game.winner.equals(resourceOwner.getName()) && game.win_recording_id != null) {
                    historyEntry.setAttribute("gameRecordingId", game.win_recording_id);
                    historyEntry.setAttribute("deckName", game.winner_deck_name);
                } else if (game.loser.equals(resourceOwner.getName()) && game.lose_recording_id != null) {
                    historyEntry.setAttribute("gameRecordingId", game.lose_recording_id);
                    historyEntry.setAttribute("deckName", game.loser_deck_name);
                }

                historyEntry.setAttribute("startTime", String.valueOf(game.GetStartDate().getTime()));
                historyEntry.setAttribute("endTime", String.valueOf(game.GetEndDate().getTime()));

                gameHistory.appendChild(historyEntry);
            }

            doc.appendChild(gameHistory);

            responseWriter.writeXmlResponse(doc);
        } else if (uri.equals("/list") && request.method() == HttpMethod.GET) {
            final List<DBDefs.GameHistory> playerGameHistory = _gameHistoryService.getTrackableGames(100);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element gameHistory = doc.createElement("gameHistory");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (DBDefs.GameHistory game : playerGameHistory) {
                Element historyEntry = doc.createElement("historyEntry");
                historyEntry.setAttribute("winner", game.winner);
                historyEntry.setAttribute("loser", game.loser);

                historyEntry.setAttribute("winReason", game.win_reason);
                historyEntry.setAttribute("loseReason", game.lose_reason);

                historyEntry.setAttribute("formatName", game.format_name);

                historyEntry.setAttribute("winnerRecordingLink", "http://www.gempukku.com/gemp-lotr/game.html?replayId="+game.winner+"$"+game.win_recording_id);
                historyEntry.setAttribute("winnerDeckName", game.winner_deck_name);

                historyEntry.setAttribute("loserRecordingLink", "http://www.gempukku.com/gemp-lotr/game.html?replayId="+game.loser+"$"+game.lose_recording_id);
                historyEntry.setAttribute("loserDeckName", game.loser_deck_name);

                historyEntry.setAttribute("startTime", dateFormat.format(new Date(game.GetStartDate().getTime())));
                historyEntry.setAttribute("endTime", dateFormat.format(new Date(game.GetEndDate().getTime())));

                gameHistory.appendChild(historyEntry);
            }

            doc.appendChild(gameHistory);

            responseWriter.writeXmlResponse(doc);
        } else if (uri.equals("/list/html") && request.method() == HttpMethod.GET) {
            final List<DBDefs.GameHistory> playerGameHistory = _gameHistoryService.getTrackableGames(100);

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

            for (DBDefs.GameHistory game : playerGameHistory) {
                String winnerLink = "http://www.gempukku.com/gemp-lotr/game.html?replayId=" + game.winner + "$" + game.win_recording_id;
                String loserLink = "http://www.gempukku.com/gemp-lotr/game.html?replayId=" + game.loser + "$" + game.lose_recording_id;

                sb.append("<tr>");
                sb.append("<td rowspan='2'>" + dateFormat.format(new Date(game.GetStartDate().getTime())) + "</td><td rowspan='2'>" + dateFormat.format(new Date(game.GetEndDate().getTime())) + "</td>");
                sb.append("<td>" + game.winner + "</td><td>" + game.win_reason + "</td><td>" + game.winner_deck_name + "</td><td><a target='_blank' href='" + winnerLink + "'>Replay</a></td>");
                sb.append("</tr>");
                sb.append("<tr>");
                sb.append("<td>" + game.loser + "</td><td>" + game.lose_reason + "</td><td>" + game.lose_reason + "</td><td><a target='_blank' href='" + loserLink + "'>Replay</a></td>");
                sb.append("</tr>");
            }

            sb.append("</table></body></html>");
            responseWriter.writeHtmlResponse(sb.toString());
        } else {
            throw new HttpProcessingException(404);
        }
    }
}
