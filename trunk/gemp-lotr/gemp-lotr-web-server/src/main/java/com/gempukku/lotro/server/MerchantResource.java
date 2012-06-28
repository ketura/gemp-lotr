package com.gempukku.lotro.server;

import com.gempukku.lotro.cards.packs.RarityReader;
import com.gempukku.lotro.cards.packs.SetRarity;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.merchant.MerchantException;
import com.gempukku.lotro.merchant.MerchantService;
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
import java.util.*;

@Singleton
@Path("/merchant")
public class MerchantResource extends AbstractResource {
    @Context
    private CollectionsManager _collectionsManager;
    @Context
    private MerchantService _merchantService;
    @Context
    private LotroCardBlueprintLibrary _library;

    private Map<String, SetRarity> _rarities;
    private SortAndFilterCards _sortAndFilterCards = new SortAndFilterCards();

    public MerchantResource() {
        _rarities = new HashMap<String, SetRarity>();
        RarityReader reader = new RarityReader();
        _rarities.put("0", reader.getSetRarity("0"));
        _rarities.put("1", reader.getSetRarity("1"));
        _rarities.put("2", reader.getSetRarity("2"));
        _rarities.put("3", reader.getSetRarity("3"));
        _rarities.put("4", reader.getSetRarity("4"));
        _rarities.put("5", reader.getSetRarity("5"));
        _rarities.put("6", reader.getSetRarity("6"));
        _rarities.put("7", reader.getSetRarity("7"));
        _rarities.put("8", reader.getSetRarity("8"));
        _rarities.put("9", reader.getSetRarity("9"));
        _rarities.put("10", reader.getSetRarity("10"));
        _rarities.put("11", reader.getSetRarity("11"));
        _rarities.put("12", reader.getSetRarity("12"));
        _rarities.put("13", reader.getSetRarity("13"));
        _rarities.put("14", reader.getSetRarity("14"));
        _rarities.put("15", reader.getSetRarity("15"));
        _rarities.put("16", reader.getSetRarity("16"));
        _rarities.put("17", reader.getSetRarity("17"));
        _rarities.put("18", reader.getSetRarity("18"));
        _rarities.put("19", reader.getSetRarity("19"));
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getCollection(
            @QueryParam("participantId") String participantId,
            @QueryParam("filter") String filter,
            @QueryParam("ownedMin") int ownedMin,
            @QueryParam("start") int start,
            @QueryParam("count") int count,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        CardCollection collection = _collectionsManager.getPlayerCollection(resourceOwner, "permanent");

        Set<CardItem> cardItems = new HashSet<CardItem>();
        final Collection<CardCollection.Item> allItems = collection.getAll().values();
        for (CardCollection.Item item : allItems) {
            if (item.getCount() >= ownedMin)
                cardItems.add(item);
        }

        if (ownedMin <= 0) {
            Set<CardItem> items = _merchantService.getSellableItems();
            for (CardItem item : items) {
                if (collection.getItemCount(item.getBlueprintId()) == 0)
                    cardItems.add(item);
            }
        }
        List<CardItem> filteredResult = _sortAndFilterCards.process(filter, cardItems, _library, _rarities);

        List<CardItem> pageToDisplay = new ArrayList<CardItem>();
        for (int i = start; i < start + count; i++) {
            if (i >= 0 && i < filteredResult.size())
                pageToDisplay.add(filteredResult.get(i));
        }

        MerchantService.PriceGuarantee priceGuarantee = _merchantService.priceCards(resourceOwner, pageToDisplay);
        Map<String, Integer> buyPrices = priceGuarantee.getBuyPrices();
        Map<String, Integer> sellPrices = priceGuarantee.getSellPrices();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element merchantElem = doc.createElement("merchant");
        merchantElem.setAttribute("currency", String.valueOf(_collectionsManager.getPlayerCollection(resourceOwner, "permanent").getCurrency()));
        merchantElem.setAttribute("count", String.valueOf(filteredResult.size()));
        doc.appendChild(merchantElem);

        for (CardItem cardItem : pageToDisplay) {
            String blueprintId = cardItem.getBlueprintId();

            Element elem;
            if (blueprintId.contains("_"))
                elem = doc.createElement("card");
            else
                elem = doc.createElement("pack");

            elem.setAttribute("count", String.valueOf(collection.getItemCount(blueprintId)));
            if (blueprintId.contains("_") && !blueprintId.endsWith("*") && collection.getItemCount(blueprintId) >= 4)
                elem.setAttribute("tradeFoil", "true");
            elem.setAttribute("blueprintId", blueprintId);
            Integer buyPrice = buyPrices.get(blueprintId);
            if (buyPrice != null && collection.getItemCount(blueprintId) > 0)
                elem.setAttribute("buyPrice", buyPrice.toString());
            Integer sellPrice = sellPrices.get(blueprintId);
            if (sellPrice != null)
                elem.setAttribute("sellPrice", sellPrice.toString());
            merchantElem.appendChild(elem);
        }

        return doc;
    }

    @Path("/buy")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document buyItem(
            @FormParam("participantId") String participantId,
            @FormParam("blueprintId") String blueprintId,
            @FormParam("price") int price,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);
        try {
            _merchantService.merchantSellsCard(resourceOwner, blueprintId, price);
            return null;
        } catch (MerchantException exp) {
            return marshalException(exp);
        }
    }

    @Path("/sell")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document sellItem(
            @FormParam("participantId") String participantId,
            @FormParam("blueprintId") String blueprintId,
            @FormParam("price") int price,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);
        try {
            _merchantService.merchantBuysCard(resourceOwner, blueprintId, price);
            return null;
        } catch (MerchantException exp) {
            return marshalException(exp);
        }
    }

    @Path("/tradeFoil")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document tradeInFoil(
            @FormParam("participantId") String participantId,
            @FormParam("blueprintId") String blueprintId,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);
        try {
            _merchantService.tradeForFoil(resourceOwner, blueprintId);
            return null;
        } catch (MerchantException exp) {
            return marshalException(exp);
        }
    }

    private Document marshalException(MerchantException e) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element error = doc.createElement("error");
        error.setAttribute("message", e.getMessage());
        doc.appendChild(error);
        return doc;
    }
}
