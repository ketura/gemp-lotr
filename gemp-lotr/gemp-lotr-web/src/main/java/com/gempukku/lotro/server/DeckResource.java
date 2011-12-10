package com.gempukku.lotro.server;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.DeckInvalidException;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.sun.jersey.spi.resource.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
@Path("/deck")
public class DeckResource extends AbstractResource {
    @Context
    private LotroServer _lotroServer;
    @Context
    private HallServer _hallServer;
    @Context
    private LotroCardBlueprintLibrary _library;
    @Context
    private DeckDAO _deckDao;

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document listDecks(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
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
        return doc;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getDeck(
            @QueryParam("deckName") String deckName,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _lotroServer.getParticipantDeck(resourceOwner, deckName);

        if (deck == null) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element deckElem = doc.createElement("deck");
            doc.appendChild(deckElem);
            return doc;
        }

        return serializeDeck(deck);
    }

    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document saveDeck(
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @FormParam("deckContents") String contents,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _lotroServer.savePlayerDeck(resourceOwner, deckName, contents);
        if (deck == null)
            sendError(Response.Status.BAD_REQUEST);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("ok");
        doc.appendChild(deckElem);

        return doc;
    }

    @Path("/rename")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document renameDeck(
            @FormParam("oldDeckName") String oldDeckName,
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _lotroServer.renamePlayerDeck(resourceOwner, oldDeckName, deckName);
        if (deck == null)
            sendError(Response.Status.NOT_FOUND);

        return serializeDeck(deck);
    }

    @Path("/delete")
    @POST
    public void deleteDeck(
            @FormParam("deckName") String deckName,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        _deckDao.deleteDeckForPlayer(resourceOwner, deckName);
    }

    @Path("/stats")
    @POST
    @Produces("text/html")
    public String getDeckStats(
            @FormParam("participantId") String participantId,
            @FormParam("deckContents") String contents,
            @Context HttpServletRequest request) {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        LotroDeck deck = _lotroServer.createTemporaryDeckForPlayer(resourceOwner, contents);
        if (deck == null)
            sendError(Response.Status.BAD_REQUEST);

        int fpCount = 0;
        int shadowCount = 0;
        for (String card : deck.getAdventureCards()) {
            Side side = _library.getLotroCardBlueprint(card).getSide();
            if (side == Side.SHADOW)
                shadowCount++;
            else if (side == Side.FREE_PEOPLE)
                fpCount++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<b>Free People</b>: " + fpCount + ", <b>Shadow</b>: " + shadowCount + "<br/>");

        for (Map.Entry<String, String> supportedFormats : _hallServer.getSupportedFormatNames().entrySet()) {
            String formatName = supportedFormats.getValue();
            LotroFormat format = _hallServer.getSupportedFormat(supportedFormats.getKey());
            try {
                format.validateDeck(deck);
                sb.append("<b>" + formatName + "</b>: <font color='green'>valid</font><br/>");
            } catch (DeckInvalidException exp) {
                sb.append("<b>" + formatName + "</b>: <font color='red'>" + exp.getMessage() + "</font><br/>");
            }
        }

        return sb.toString();
    }

    private Document serializeDeck(LotroDeck deck) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element deckElem = doc.createElement("deck");
        doc.appendChild(deckElem);

        Element ringBearer = doc.createElement("ringBearer");
        ringBearer.setAttribute("blueprintId", deck.getRingBearer());
        deckElem.appendChild(ringBearer);

        Element ring = doc.createElement("ring");
        ring.setAttribute("blueprintId", deck.getRing());
        deckElem.appendChild(ring);

        for (String s : deck.getSites()) {
            Element site = doc.createElement("site");
            site.setAttribute("blueprintId", s);
            deckElem.appendChild(site);
        }
        for (String s : deck.getAdventureCards()) {
            Element card = doc.createElement("card");
            card.setAttribute("side", _library.getLotroCardBlueprint(s).getSide().toString());
            card.setAttribute("blueprintId", s);
            deckElem.appendChild(card);
        }

        return doc;
    }
}
