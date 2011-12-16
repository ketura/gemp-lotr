package com.gempukku.lotro.server;

import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.hall.HallException;
import com.gempukku.lotro.hall.HallInfoVisitor;
import com.gempukku.lotro.hall.HallServer;
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

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getHall(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element hall = doc.createElement("hall");
        String motd = _hallServer.getMOTD();
        if (motd != null)
            hall.setAttribute("motd", motd);

        _hallServer.processTables(resourceOwner, new SerializeHallInfoVisitor(doc, hall));
        for (Map.Entry<String, String> format : _hallServer.getSupportedFormatNames().entrySet()) {
            Element formatElem = doc.createElement("format");
            formatElem.setAttribute("type", format.getKey());
            formatElem.appendChild(doc.createTextNode(format.getValue()));
            hall.appendChild(formatElem);
        }
        for (League league : _hallServer.getRunningLeagues()) {
            Element formatElem = doc.createElement("format");
            formatElem.setAttribute("type", league.getType());
            formatElem.appendChild(doc.createTextNode(league.getName()));
            hall.appendChild(formatElem);
        }

        doc.appendChild(hall);

        processDeliveryServiceNotification(request, response);

        return doc;
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

    @Path("/leave")
    @POST
    public void leaveTable(
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _hallServer.leaveAwaitingTables(resourceOwner);
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
        public void playerIsWaiting(boolean waiting) {
            _hall.setAttribute("waiting", String.valueOf(waiting));
        }

        @Override
        public void visitTable(String tableId, String gameId, String tableStatus, String formatName, Set<String> playerIds, String winner) {
            Element table = _doc.createElement("table");
            table.setAttribute("id", tableId);
            if (gameId != null)
                table.setAttribute("gameId", gameId);
            table.setAttribute("status", tableStatus);
            table.setAttribute("format", formatName);
            table.setAttribute("players", mergeStrings(playerIds));
            if (winner != null)
                table.setAttribute("winner", winner);
            _hall.appendChild(table);
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
