package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.mtg.MtgCardServer;
import com.gempukku.mtg.ProviderNotFoundException;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

public class MtgCardsRequestHandler implements UriRequestHandler {
    private MtgCardServer _mtgCardServer;

    public MtgCardsRequestHandler(Map<Type, Object> context) {
        _mtgCardServer = (MtgCardServer) context.get(MtgCardServer.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String provider = getQueryParameterSafely(queryDecoder, "provider");
        String updateMarker = getQueryParameterSafely(queryDecoder, "update");

        if (provider != null) {
            processCardListRequest(responseWriter, provider, updateMarker);
        } else {
            processProviderListRequest(responseWriter);
        }
    }

    private void processProviderListRequest(ResponseWriter responseWriter) {
        byte[] dataProvidersResponse = _mtgCardServer.getDataProvidersResponse();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(CONTENT_TYPE, "application/json; charset=UTF-8");

        responseWriter.writeByteResponse(dataProvidersResponse, headers);
    }

    private void processCardListRequest(ResponseWriter responseWriter, String provider, String updateMarker) throws Exception {
        try {
            MtgCardServer.CardDatabaseHolder cardDatabaseHolder = _mtgCardServer.getCardDatabaseHolder(provider);
            if (cardDatabaseHolder == null)
                throw new HttpProcessingException(204);
            else if (updateMarker != null && updateMarker.equals(String.valueOf(cardDatabaseHolder.getUpdateDate())))
                throw new HttpProcessingException(304);

            Map<String, String> headers = new HashMap<String, String>();
            headers.put(CONTENT_TYPE, "application/json; charset=UTF-8");
            headers.put(HttpHeaders.Names.ETAG, String.valueOf(cardDatabaseHolder.getUpdateDate()));

            responseWriter.writeByteResponse(cardDatabaseHolder.getBytes(), headers);
        } catch (ProviderNotFoundException exp) {
            throw new HttpProcessingException(404);
        }
    }

    protected String getQueryParameterSafely(QueryStringDecoder queryStringDecoder, String parameterName) {
        List<String> parameterValues = queryStringDecoder.getParameters().get(parameterName);
        if (parameterValues != null && parameterValues.size() > 0)
            return parameterValues.get(0);
        else
            return null;
    }
}
