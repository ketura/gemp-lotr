package com.gempukku.lotro.async.handler;

import com.alibaba.fastjson.JSON;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.common.JSONDefs;
import com.gempukku.lotro.game.GameHistoryService;
import com.gempukku.lotro.game.Player;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.log4j.Logger;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class ServerStatsRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private final GameHistoryService _gameHistoryService;

    private static final Logger _log = Logger.getLogger(ServerStatsRequestHandler.class);

    public ServerStatsRequestHandler(Map<Type, Object> context) {
        super(context);

        _gameHistoryService = extractObject(context, GameHistoryService.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.equals("") && request.method() == HttpMethod.GET) {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
            String participantId = getQueryParameterSafely(queryDecoder, "participantId");
            String startDay = getQueryParameterSafely(queryDecoder, "startDay");
            String length = getQueryParameterSafely(queryDecoder, "length");

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                long from = format.parse(startDay).getTime();
                Date to = format.parse(startDay);
                switch (length) {
                    case "month" -> to.setMonth(to.getMonth() + 1);
                    case "week" -> to.setDate(to.getDate() + 7);
                    case "day" -> to.setDate(to.getDate() + 1);
                    default -> throw new HttpProcessingException(400);
                }
                long duration = to.getTime() - from;

                var stats = new JSONDefs.PlayHistoryStats();
                stats.ActivePlayers = _gameHistoryService.getActivePlayersCount(from, duration);
                stats.GamesCount = _gameHistoryService.getGamesPlayedCount(from, duration);
                stats.StartDate = format.format(new Date(from));
                stats.EndDate = format.format(new Date(from + duration - 1));
                stats.Stats = _gameHistoryService.getGameHistoryStatistics(from, duration);

                responseWriter.writeJsonResponse(JSON.toJSONString(stats));
            } catch (ParseException exp) {
                logHttpError(_log, 400, request.uri(), exp);
                throw new HttpProcessingException(400);
            }
        } else {
            throw new HttpProcessingException(404);
        }
    }
}
