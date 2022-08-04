package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.game.GameRecorder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AsciiString;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;

public class ReplayRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private final GameRecorder _gameRecorder;

    public ReplayRequestHandler(Map<Type, Object> context) {
        super(context);

        _gameRecorder = extractObject(context, GameRecorder.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.startsWith("/") && request.method() == HttpMethod.GET) {
            String replayId = uri.substring(1);

            if (!replayId.contains("$"))
                throw new HttpProcessingException(404);
            if (replayId.contains("."))
                throw new HttpProcessingException(404);

            final String[] split = replayId.split("\\$");
            if (split.length != 2)
                throw new HttpProcessingException(404);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            final InputStream recordedGame = _gameRecorder.getRecordedGame(split[0], split[1]);
            try (recordedGame) {
                if (recordedGame == null)
                    throw new HttpProcessingException(404);
                byte[] bytes = new byte[1024];
                int count;
                while ((count = recordedGame.read(bytes)) != -1)
                    baos.write(bytes, 0, count);
            } catch (IOException exp) {
                throw new HttpProcessingException(404);
            }

            Map<AsciiString, String> headers = new HashMap<>();
            headers.put(CONTENT_TYPE, "application/html; charset=UTF-8");

            responseWriter.writeByteResponse(baos.toByteArray(), headers);
        } else {
            throw new HttpProcessingException(404);
        }
    }
}
