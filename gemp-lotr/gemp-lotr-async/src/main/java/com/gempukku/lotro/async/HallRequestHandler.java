package com.gempukku.lotro.async;

import com.gempukku.lotro.SubscriptionConflictException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallChannelVisitor;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.util.Map;

public class HallRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private CollectionsManager _collectionManager;
    private LotroFormatLibrary _formatLibrary;
    private HallServer _hallServer;
    private LeagueService _leagueService;
    private long _longPollingLength = 5000;
    private long _longPollingInterval = 200;

    public HallRequestHandler(Map<Type, Object> context) {
        super(context);
        _collectionManager = extractObject(context, CollectionsManager.class);
        _formatLibrary = extractObject(context, LotroFormatLibrary.class);
        _hallServer = extractObject(context, HallServer.class);
        _leagueService = extractObject(context, LeagueService.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) {
        if (uri.equals("") && request.getMethod() == HttpMethod.GET) {
            getHall(request, responseWriter);
        } else if (uri.equals("/update") && request.getMethod() == HttpMethod.POST) {
            updateHall(request, responseWriter);
        } else {
            responseWriter.writeError(404);
        }
    }

    private void getHall(HttpRequest request, ResponseWriter responseWriter) {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());

        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        try {
            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            Element hall = doc.createElement("hall");
            hall.setAttribute("currency", String.valueOf(_collectionManager.getPlayerCollection(resourceOwner, "permanent").getCurrency()));

            _hallServer.signupUserForHall(resourceOwner, new SerializeHallInfoVisitor(doc, hall));
            for (Map.Entry<String, LotroFormat> format : _formatLibrary.getHallFormats().entrySet()) {
                Element formatElem = doc.createElement("format");
                formatElem.setAttribute("type", format.getKey());
                formatElem.appendChild(doc.createTextNode(format.getValue().getName()));
                hall.appendChild(formatElem);
            }
            for (League league : _leagueService.getActiveLeagues()) {
                final LeagueSerieData currentLeagueSerie = _leagueService.getCurrentLeagueSerie(league);
                if (currentLeagueSerie != null && _leagueService.isPlayerInLeague(league, resourceOwner)) {
                    Element formatElem = doc.createElement("format");
                    formatElem.setAttribute("type", league.getType());
                    formatElem.appendChild(doc.createTextNode(league.getName()));
                    hall.appendChild(formatElem);
                }
            }

            doc.appendChild(hall);

            responseWriter.writeResponse(doc);
        } catch (HttpProcessingException exp) {
            responseWriter.writeError(exp.getStatus());
        } catch (Exception exp) {
            responseWriter.writeError(500);
        }
    }

    private void updateHall(HttpRequest request, ResponseWriter responseWriter) {
        try {
            HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
            String participantId = getFormParameterSafely(postDecoder, "participantId");
            int channelNumber = Integer.parseInt(getFormParameterSafely(postDecoder, "channelNumber"));

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            Element hall = doc.createElement("hall");

            try {
                // Use long polling
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() < start + _longPollingLength && !_hallServer.hasChanges(resourceOwner, channelNumber)) {
                    try {
                        Thread.sleep(_longPollingInterval);
                    } catch (InterruptedException exp) {

                    }
                }
                _hallServer.processHall(resourceOwner, channelNumber, new SerializeHallInfoVisitor(doc, hall));
            } catch (SubscriptionExpiredException exp) {
                throw new WebApplicationException(Response.Status.GONE);
            } catch (SubscriptionConflictException exp) {
                throw new WebApplicationException(Response.Status.CONFLICT);
            }
            hall.setAttribute("currency", String.valueOf(_collectionManager.getPlayerCollection(resourceOwner, "permanent").getCurrency()));

            doc.appendChild(hall);
            responseWriter.writeResponse(doc);
        } catch (HttpProcessingException exp) {
            responseWriter.writeError(exp.getStatus());
        } catch (Exception exp) {
            responseWriter.writeError(500);
        }
    }


    private class SerializeHallInfoVisitor implements HallChannelVisitor {
        private Document _doc;
        private Element _hall;

        public SerializeHallInfoVisitor(Document doc, Element hall) {
            _doc = doc;
            _hall = hall;
        }

        @Override
        public void channelNumber(int channelNumber) {
            _hall.setAttribute("channelNumber", String.valueOf(channelNumber));
        }

        @Override
        public void serverTime(String serverTime) {
            _hall.setAttribute("serverTime", serverTime);
        }

        @Override
        public void motdChanged(String motd) {
            _hall.setAttribute("motd", motd);
        }

        @Override
        public void addTournamentQueue(String queueId, Map<String, String> props) {
            Element queue = _doc.createElement("queue");
            queue.setAttribute("action", "add");
            queue.setAttribute("id", queueId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                queue.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(queue);
        }

        @Override
        public void updateTournamentQueue(String queueId, Map<String, String> props) {
            Element queue = _doc.createElement("queue");
            queue.setAttribute("action", "update");
            queue.setAttribute("id", queueId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                queue.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(queue);
        }

        @Override
        public void removeTournamentQueue(String queueId) {
            Element queue = _doc.createElement("queue");
            queue.setAttribute("action", "remove");
            queue.setAttribute("id", queueId);
            _hall.appendChild(queue);
        }

        @Override
        public void addTournament(String tournamentId, Map<String, String> props) {
            Element tournament = _doc.createElement("tournament");
            tournament.setAttribute("action", "add");
            tournament.setAttribute("id", tournamentId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                tournament.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(tournament);
        }

        @Override
        public void updateTournament(String tournamentId, Map<String, String> props) {
            Element tournament = _doc.createElement("tournament");
            tournament.setAttribute("action", "update");
            tournament.setAttribute("id", tournamentId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                tournament.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(tournament);
        }

        @Override
        public void removeTournament(String tournamentId) {
            Element tournament = _doc.createElement("tournament");
            tournament.setAttribute("action", "remove");
            tournament.setAttribute("id", tournamentId);
            _hall.appendChild(tournament);
        }

        @Override
        public void addTable(String tableId, Map<String, String> props) {
            Element table = _doc.createElement("table");
            table.setAttribute("action", "add");
            table.setAttribute("id", tableId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                table.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(table);
        }

        @Override
        public void updateTable(String tableId, Map<String, String> props) {
            Element table = _doc.createElement("table");
            table.setAttribute("action", "update");
            table.setAttribute("id", tableId);
            for (Map.Entry<String, String> attribute : props.entrySet())
                table.setAttribute(attribute.getKey(), attribute.getValue());
            _hall.appendChild(table);
        }

        @Override
        public void removeTable(String tableId) {
            Element table = _doc.createElement("table");
            table.setAttribute("action", "remove");
            table.setAttribute("id", tableId);
            _hall.appendChild(table);
        }

        @Override
        public void runningPlayerGame(String gameId) {
            Element runningGame = _doc.createElement("game");
            runningGame.setAttribute("id", gameId);
            _hall.appendChild(runningGame);
        }

        @Override
        public void playerBusy(boolean busy) {
            _hall.setAttribute("busy", String.valueOf(busy));
        }
    }
}
