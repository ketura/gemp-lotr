package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.mtg.MtgCardServer;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.IF_NONE_MATCH;

public class MtgCardsRequestHandler implements UriRequestHandler {
    private MtgCardServer _mtgCardServer;

    public MtgCardsRequestHandler(Map<Type, Object> context) {
        _mtgCardServer = (MtgCardServer) context.get(MtgCardServer.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        MtgCardServer.CardDatabaseHolder cardDatabaseHolder = _mtgCardServer.getCardDatabaseHolder();
        if (cardDatabaseHolder == null || clientHasCurrentVersion(request, cardDatabaseHolder.getUpdateMarker())) {
            responseWriter.writeError(304);
            return;
        }

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(CONTENT_TYPE, "application/json; charset=UTF-8");
        headers.put(HttpHeaders.Names.ETAG, cardDatabaseHolder.getUpdateMarker());

        responseWriter.writeByteResponse(cardDatabaseHolder.getBytes(), headers);
    }

    private boolean clientHasCurrentVersion(HttpRequest request, String version) {
        String ifNoneMatch = request.getHeader(IF_NONE_MATCH);
        if (ifNoneMatch != null) {
            String[] clientKnownVersions = ifNoneMatch.split(",");
            for (String clientKnownVersion : clientKnownVersions) {
                if (clientKnownVersion.trim().equals(version))
                    return true;
            }
        }
        return false;
    }
}
