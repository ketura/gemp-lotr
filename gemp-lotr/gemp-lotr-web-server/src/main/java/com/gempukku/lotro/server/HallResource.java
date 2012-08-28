package com.gempukku.lotro.server;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallException;
import com.gempukku.lotro.hall.HallInfoVisitor;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.logic.GameUtils;
import com.sun.jersey.spi.resource.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;
import java.util.Set;

@Singleton
@Path("/hall")
public class HallResource extends AbstractResource {
    @Context
    private HallServer _hallServer;
    @Context
    private LeagueService _leagueService;
    @Context
    private LotroFormatLibrary _formatLibrary;
    @Context
    private LotroCardBlueprintLibrary _library;

    @Path("/formats/html")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getFormats(
            @Context HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");

        StringBuilder result = new StringBuilder();
        for (LotroFormat lotroFormat : _formatLibrary.getHallFormats().values()) {
            result.append("<b>" + lotroFormat.getName() + "</b>");
            result.append("<ul>");
            result.append("<li>valid sets: ");
            for (Integer integer : lotroFormat.getValidSets())
                result.append(integer + ", ");
            result.append("</li>");
            result.append("<li>sites from block: " + lotroFormat.getSiteBlock().getHumanReadable() + "</li>");
            result.append("<li>Ring-bearer skirmish can be cancelled: " + (lotroFormat.canCancelRingBearerSkirmish() ? "yes" : "no") + "</li>");
            result.append("<li>X-listed: ");
            for (String blueprintId : lotroFormat.getBannedCards())
                result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
            if (lotroFormat.getBannedCards().size() == 0)
                result.append("none,");
            result.append("</li>");
            result.append("<li>R-listed: ");
            for (String blueprintId : lotroFormat.getRestrictedCards())
                result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
            if (lotroFormat.getRestrictedCards().size() == 0)
                result.append("none,");
            result.append("</li>");
            result.append("<li>Additional valid: ");
            for (String blueprintId : lotroFormat.getValidCards())
                result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
            if (lotroFormat.getValidCards().size() == 0)
                result.append("none,");
            result.append("</li>");
            result.append("</ul>");
        }

        return result.toString();
    }

    @Path("/format/{format}")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getFormat(
            @PathParam("format") String format,
            @Context HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");

        StringBuilder result = new StringBuilder();
        LotroFormat lotroFormat = _formatLibrary.getFormat(format);
        result.append("<b>" + lotroFormat.getName() + "</b>");
        result.append("<ul>");
        result.append("<li>valid sets: ");
        for (Integer integer : lotroFormat.getValidSets())
            result.append(integer + ", ");
        result.append("</li>");
        result.append("<li>sites from block: " + lotroFormat.getSiteBlock().getHumanReadable() + "</li>");
        result.append("<li>Ring-bearer skirmish can be cancelled: " + (lotroFormat.canCancelRingBearerSkirmish() ? "yes" : "no") + "</li>");
        result.append("<li>X-listed: ");
        for (String blueprintId : lotroFormat.getBannedCards())
            result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
        if (lotroFormat.getBannedCards().size() == 0)
            result.append("none,");
        result.append("</li>");
        result.append("<li>R-listed: ");
        for (String blueprintId : lotroFormat.getRestrictedCards())
            result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
        if (lotroFormat.getRestrictedCards().size() == 0)
            result.append("none,");
        result.append("</li>");
        result.append("<li>Additional valid: ");
        for (String blueprintId : lotroFormat.getValidCards())
            result.append(GameUtils.getCardLink(blueprintId, _library.getLotroCardBlueprint(blueprintId)) + ", ");
        if (lotroFormat.getValidCards().size() == 0)
            result.append("none,");
        result.append("</li>");
        result.append("</ul>");

        return result.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getHall(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException, Exception {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element hall = doc.createElement("hall");
        hall.setAttribute("currency", String.valueOf(_collectionManager.getPlayerCollection(resourceOwner, "permanent").getCurrency()));
        String motd = _hallServer.getMOTD();
        if (motd != null)
            hall.setAttribute("motd", motd);

        _hallServer.processHall(resourceOwner, new SerializeHallInfoVisitor(doc, hall));
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

        processLoginReward(request);
        processDeliveryServiceNotification(request, response);

        return doc;
    }

    @Path("/queue/{queue}")
    @POST
    public Document joinQueue(
            @PathParam("queue") String queueId,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            _hallServer.joinQueue(queueId, resourceOwner, deckName);
            return null;
        } catch (HallException e) {
            return marshalException(e);
        }
    }

    @Path("/queue/leave")
    @POST
    public void leaveQueue(
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _hallServer.leaveQueues(resourceOwner);
    }

    @Path("/{table}")
    @POST
    public Document joinTable(
            @PathParam("table") String tableId,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            _hallServer.joinTableAsPlayer(tableId, resourceOwner, deckName);
            return null;
        } catch (HallException e) {
            return marshalException(e);
        }
    }

    @Path("/leave")
    @POST
    public void leaveTable(
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _hallServer.leaveAwaitingTables(resourceOwner);
    }

    @POST
    public Document createTable(
            @FormParam("format") String format,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        try {
            _hallServer.createNewTable(format, resourceOwner, deckName);
            return null;
        } catch (HallException e) {
            return marshalException(e);
        }
    }

    private Document marshalException(HallException e) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element error = doc.createElement("error");
        error.setAttribute("message", e.getMessage());
        doc.appendChild(error);
        return doc;
    }

    private class SerializeHallInfoVisitor implements HallInfoVisitor {
        private Document _doc;
        private Element _hall;

        public SerializeHallInfoVisitor(Document doc, Element hall) {
            _doc = doc;
            _hall = hall;
        }

        @Override
        public void serverTime(String time) {
            _hall.setAttribute("serverTime", time);
        }

        @Override
        public void playerIsWaiting(boolean waiting) {
            _hall.setAttribute("waiting", String.valueOf(waiting));
        }

        @Override
        public void visitTable(String tableId, String gameId, boolean watchable, String tableStatus, String formatName, String tournamentName, Set<String> playerIds, String winner) {
            Element table = _doc.createElement("table");
            table.setAttribute("id", tableId);
            if (gameId != null)
                table.setAttribute("gameId", gameId);
            table.setAttribute("status", tableStatus);
            table.setAttribute("watchable", String.valueOf(watchable));
            table.setAttribute("format", formatName);
            table.setAttribute("tournament", tournamentName);
            table.setAttribute("players", mergeStrings(playerIds));
            if (winner != null)
                table.setAttribute("winner", winner);
            _hall.appendChild(table);
        }

        @Override
        public void runningPlayerDraft(String draftId) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void visitTournamentQueue(String tournamentQueueKey, int cost, String collectionName, String formatName,
                                         String tournamentQueueName, int playerCount, boolean playerSignedUp) {
            Element tournamentQueue = _doc.createElement("tournamentQueue");
            tournamentQueue.setAttribute("id", tournamentQueueKey);
            tournamentQueue.setAttribute("cost", String.valueOf(cost));
            tournamentQueue.setAttribute("tournament", tournamentQueueName);
            tournamentQueue.setAttribute("players", String.valueOf(playerCount));
            tournamentQueue.setAttribute("format", formatName);
            tournamentQueue.setAttribute("joined", String.valueOf(playerSignedUp));
            _hall.appendChild(tournamentQueue);
        }

        @Override
        public void runningPlayerGame(String gameId) {
            Element runningGame = _doc.createElement("game");
            runningGame.setAttribute("id", gameId);
            _hall.appendChild(runningGame);
        }
    }

    private String mergeStrings(Set<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings)
            sb.append(string).append(",");
        if (sb.length() > 0)
            return sb.deleteCharAt(sb.length() - 1).toString();
        return sb.toString();
    }
}
