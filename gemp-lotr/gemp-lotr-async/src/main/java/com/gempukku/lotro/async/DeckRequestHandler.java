package com.gempukku.lotro.async;

import com.gempukku.lotro.db.DeckDAO;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DeckRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private DeckDAO _deckDao;

    public DeckRequestHandler(Map<Type, Object> context) {
        super(context);
        _deckDao = extractObject(context, DeckDAO.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) {
        if (uri.equals("/list") && request.getMethod() == HttpMethod.GET) {
            listDecks(request, responseWriter);
        } else {
            responseWriter.writeError(404);
        }
    }

    private void listDecks(HttpRequest request, ResponseWriter responseWriter) {
        try {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
            String participantId = getQueryParameterSafely(queryDecoder, "participantId");

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            List<String> names = new ArrayList<String>(_deckDao.getPlayerDeckNames(resourceOwner));
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
            responseWriter.writeResponse(doc);
        } catch (Exception exp) {
            responseWriter.writeError(500);
        }
    }
}
