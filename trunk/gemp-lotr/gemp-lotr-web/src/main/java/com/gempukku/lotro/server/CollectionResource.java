package com.gempukku.lotro.server;

import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.packs.PacksStorage;
import com.sun.jersey.spi.resource.Singleton;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

@Singleton
@Path("/collection")
public class CollectionResource extends AbstractResource {
    private static final Logger _logger = Logger.getLogger(CollectionResource.class);

    @Context
    private LotroServer _lotroServer;
    @Context
    private CollectionDAO _collectionDao;
    @Context
    private LotroCardBlueprintLibrary _library;
    @Context
    private LeagueService _leagueService;
    @Context
    private PacksStorage _packStorage;

    @Path("/{collectionType}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getCollection(
            @PathParam("collectionType") String collectionType,
            @QueryParam("participantId") String participantId,
            @QueryParam("filter") String filter,
            @QueryParam("start") int start,
            @QueryParam("count") int count,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        CardCollection collection = getCollection(resourceOwner, collectionType);
        if (collection == null)
            sendError(Response.Status.NOT_FOUND);

        List<CardCollection.Item> filteredResult = collection.getItems(filter, _library);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionElem = doc.createElement("collection");
        collectionElem.setAttribute("count", String.valueOf(filteredResult.size()));
        doc.appendChild(collectionElem);

        int index = 0;
        for (CardCollection.Item item : filteredResult) {
            if (index >= start && index < start + count) {
                String blueprintId = item.getBlueprintId();
                if (item.getType() == CardCollection.Item.Type.CARD) {
                    Element card = doc.createElement("card");
                    card.setAttribute("count", String.valueOf(item.getCount()));
                    card.setAttribute("blueprintId", blueprintId);
                    Side side = _library.getLotroCardBlueprint(blueprintId).getSide();
                    if (side != null)
                        card.setAttribute("side", side.toString());
                    collectionElem.appendChild(card);
                } else {
                    Element pack = doc.createElement("pack");
                    pack.setAttribute("count", String.valueOf(item.getCount()));
                    pack.setAttribute("blueprintId", blueprintId);
                    collectionElem.appendChild(pack);
                }
            }
            index++;
        }

        processDeliveryServiceNotification(request, response);

        return doc;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getCollectionTypes(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionsElem = doc.createElement("collections");

        for (League league : _leagueService.getActiveLeagues()) {
            Element collectionElem = doc.createElement("collection");
            collectionElem.setAttribute("type", league.getType());
            collectionElem.setAttribute("name", league.getName());
            collectionsElem.appendChild(collectionElem);
        }

        doc.appendChild(collectionsElem);
        return doc;
    }

    private CardCollection getCollection(Player player, String collectionType) {
        CardCollection collection = null;
        if (collectionType.equals("default"))
            collection = _lotroServer.getDefaultCollection();
        else if (collectionType.equals("permanent")) {
            collection = _collectionDao.getCollectionForPlayer(player, "permanent");
            if (collection == null)
                collection = new DefaultCardCollection();
        } else {
            League league = _leagueService.getLeagueByType(collectionType);
            if (league != null)
                collection = _leagueService.getLeagueCollection(player, league);
        }
        return collection;
    }

    @Path("/{collectionType}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document openPack(
            @PathParam("collectionType") String collectionType,
            @FormParam("participantId") String participantId,
            @FormParam("pack") String packId,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        CardCollection collection = getCollection(resourceOwner, collectionType);
        if (collection == null || !(collection instanceof MutableCardCollection))
            sendError(Response.Status.NOT_FOUND);

        MutableCardCollection modifiableColleciton = (MutableCardCollection) collection;

        List<CardCollection.Item> packContents = modifiableColleciton.openPack(packId, _packStorage);
        _deliveryService.addPackage(resourceOwner, collectionType, packContents);

        if (packContents == null)
            sendError(Response.Status.NOT_FOUND);

        _collectionDao.setCollectionForPlayer(resourceOwner, collectionType, modifiableColleciton);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionElem = doc.createElement("pack");
        doc.appendChild(collectionElem);

        for (CardCollection.Item item : packContents) {
            String blueprintId = item.getBlueprintId();
            if (item.getType() == CardCollection.Item.Type.CARD) {
                Element card = doc.createElement("card");
                card.setAttribute("count", String.valueOf(item.getCount()));
                card.setAttribute("blueprintId", blueprintId);
                Side side = _library.getLotroCardBlueprint(blueprintId).getSide();
                if (side != null)
                    card.setAttribute("side", side.toString());
                collectionElem.appendChild(card);
            } else {
                Element pack = doc.createElement("pack");
                pack.setAttribute("count", String.valueOf(item.getCount()));
                pack.setAttribute("blueprintId", blueprintId);
                collectionElem.appendChild(pack);
            }
        }

        processDeliveryServiceNotification(request, response);

        return doc;
    }
}
