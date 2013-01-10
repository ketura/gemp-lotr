package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionConflictException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.LongPollingResource;
import com.gempukku.lotro.async.LongPollingSystem;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.LotroGameMediator;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.game.ParticipantCommunicationVisitor;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.state.EventSerializer;
import com.gempukku.lotro.game.state.GameEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private LotroServer _lotroServer;
    private Set<Phase> _autoPassDefault = new HashSet<Phase>();
    private LongPollingSystem _longPollingSystem;

    public GameRequestHandler(Map<Type, Object> context) {
        super(context);
        _lotroServer = extractObject(context, LotroServer.class);
        _longPollingSystem = extractObject(context, LongPollingSystem.class);

        _autoPassDefault.add(Phase.FELLOWSHIP);
        _autoPassDefault.add(Phase.MANEUVER);
        _autoPassDefault.add(Phase.ARCHERY);
        _autoPassDefault.add(Phase.ASSIGNMENT);
        _autoPassDefault.add(Phase.REGROUP);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.startsWith("/") && uri.endsWith("/cardInfo") && request.getMethod() == HttpMethod.GET) {
            getCardInfo(request, uri.substring(1, uri.length() - 9), responseWriter);
        } else if (uri.startsWith("/") && uri.endsWith("/concede") && request.getMethod() == HttpMethod.POST) {
            concede(request, uri.substring(1, uri.length() - 8), responseWriter);
        } else if (uri.startsWith("/") && uri.endsWith("/cancel") && request.getMethod() == HttpMethod.POST) {
            cancel(request, uri.substring(1, uri.length() - 7), responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.GET) {
            getGameState(request, uri.substring(1), responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.POST) {
            updateGameState(request, uri.substring(1), responseWriter);
        } else {
            responseWriter.writeError(404);
        }
    }

    private void updateGameState(HttpRequest request, String gameId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        int channelNumber = Integer.parseInt(getFormParameterSafely(postDecoder, "channelNumber"));
        Integer decisionId = null;
        String decisionIdStr = getFormParameterSafely(postDecoder, "decisionId");
        if (decisionIdStr != null)
            decisionId = Integer.parseInt(decisionIdStr);
        String decisionValue = getFormParameterSafely(postDecoder, "decisionValue");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            throw new HttpProcessingException(404);

        gameMediator.setPlayerAutoPassSettings(resourceOwner.getName(), getAutoPassPhases(request));

        if (decisionId != null)
            gameMediator.playerAnswered(resourceOwner, channelNumber, decisionId, decisionValue);

        GameUpdateLongPollingResource pollingResource = new GameUpdateLongPollingResource(channelNumber, gameMediator, resourceOwner, responseWriter);

        if (pollingResource.isChanged())
            pollingResource.process();
        else
            _longPollingSystem.appendLongPollingResource(pollingResource);
    }

    private class GameUpdateLongPollingResource implements LongPollingResource {
        private LotroGameMediator _gameMediator;
        private Player _resourceOwner;
        private int _channelNumber;
        private ResponseWriter _responseWriter;

        private GameUpdateLongPollingResource(int channelNumber, LotroGameMediator gameMediator, Player resourceOwner, ResponseWriter responseWriter) {
            _channelNumber = channelNumber;
            _gameMediator = gameMediator;
            _resourceOwner = resourceOwner;
            _responseWriter = responseWriter;
        }

        @Override
        public boolean isChanged() {
            try {
                return _gameMediator.hasAnyNewMessages(_resourceOwner, _channelNumber);
            } catch (PrivateInformationException e) {
                return true;
            } catch (SubscriptionExpiredException e) {
                return true;
            } catch (SubscriptionConflictException e) {
                return true;
            }
        }

        @Override
        public void process() {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document doc = documentBuilder.newDocument();
                Element update = doc.createElement("update");

                _gameMediator.processCommunicationChannel(_resourceOwner, _channelNumber, new SerializationVisitor(doc, update));

                doc.appendChild(update);

                _responseWriter.writeXmlResponse(doc);
            } catch (SubscriptionConflictException exp) {
                _responseWriter.writeError(409);
            } catch (PrivateInformationException e) {
                _responseWriter.writeError(403);
            } catch (SubscriptionExpiredException e) {
                _responseWriter.writeError(410);
            } catch (Exception e) {
                _responseWriter.writeError(500);
            }
        }

    }

    private void cancel(HttpRequest request, String gameId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            throw new HttpProcessingException(404);

        gameMediator.cancel(resourceOwner);

        responseWriter.writeXmlResponse(null);
    }

    private void concede(HttpRequest request, String gameId, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            throw new HttpProcessingException(404);

        gameMediator.concede(resourceOwner);

        responseWriter.writeXmlResponse(null);
    }

    private void getCardInfo(HttpRequest request, String gameId, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");
        int cardId = Integer.parseInt(getQueryParameterSafely(queryDecoder, "cardId"));

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);
        if (gameMediator == null)
            throw new HttpProcessingException(404);

        responseWriter.writeHtmlResponse(gameMediator.produceCardInfo(resourceOwner, cardId));
    }

    private void getGameState(HttpRequest request, String gameId, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroGameMediator gameMediator = _lotroServer.getGameById(gameId);

        if (gameMediator == null)
            throw new HttpProcessingException(404);

        gameMediator.setPlayerAutoPassSettings(resourceOwner.getName(), getAutoPassPhases(request));

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element gameState = doc.createElement("gameState");

        try {
            gameMediator.singupUserForGame(resourceOwner, new SerializationVisitor(doc, gameState));
        } catch (PrivateInformationException e) {
            throw new HttpProcessingException(403);
        }

        doc.appendChild(gameState);

        responseWriter.writeXmlResponse(doc);
    }

    private Set<Phase> getAutoPassPhases(HttpRequest request) {
        CookieDecoder cookieDecoder = new CookieDecoder();
        Set<Cookie> cookies = cookieDecoder.decode(request.getHeader(HttpHeaders.Names.COOKIE));
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("autoPassPhases")) {
                final String[] phases = cookie.getValue().split("0");
                Set<Phase> result = new HashSet<Phase>();
                for (String phase : phases)
                    result.add(Phase.valueOf(phase));
                return result;
            }
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("autoPass") && cookie.getValue().equals("false"))
                return Collections.emptySet();
        }
        return _autoPassDefault;
    }

    private class SerializationVisitor implements ParticipantCommunicationVisitor {
        private Document _doc;
        private Element _element;
        private EventSerializer _eventSerializer = new EventSerializer();

        private SerializationVisitor(Document doc, Element element) {
            _doc = doc;
            _element = element;
        }

        @Override
        public void visitChannelNumber(int channelNumber) {
            _element.setAttribute("cn", String.valueOf(channelNumber));
        }

        @Override
        public void visitGameEvent(GameEvent gameEvent) {
            _element.appendChild(_eventSerializer.serializeEvent(_doc, gameEvent));
        }

        @Override
        public void visitClock(Map<String, Integer> secondsLeft) {
            _element.appendChild(serializeClocks(_doc, secondsLeft));
        }
    }

    private Node serializeClocks(Document doc, Map<String, Integer> secondsLeft) {
        Element clocks = doc.createElement("clocks");
        for (Map.Entry<String, Integer> userClock : secondsLeft.entrySet()) {
            Element clock = doc.createElement("clock");
            clock.setAttribute("participantId", userClock.getKey());
            clock.appendChild(doc.createTextNode(userClock.getValue().toString()));
            clocks.appendChild(clock);
        }

        return clocks;
    }
}
