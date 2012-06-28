package com.gempukku.lotro.server;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.Player;
import com.sun.jersey.spi.resource.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;

@Singleton
@Path("/delivery")
public class DeliveryResource extends AbstractResource {

    @Context
    private LotroCardBlueprintLibrary _library;

    @GET
    public Document getDelivery(
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, null);
        Map<String, ? extends CardCollection> delivery = _deliveryService.consumePackages(resourceOwner);
        if (delivery == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element deliveryElem = doc.createElement("delivery");
        for (Map.Entry<String, ? extends CardCollection> collectionTypeItems : delivery.entrySet()) {
            String collectionType = collectionTypeItems.getKey();
            CardCollection items = collectionTypeItems.getValue();

            Element collectionTypeElem = doc.createElement("collectionType");
            collectionTypeElem.setAttribute("name", collectionType);
            for (CardCollection.Item item : items.getAll().values()) {
                String blueprintId = item.getBlueprintId();
                if (item.getType() == CardCollection.Item.Type.CARD) {
                    Element card = doc.createElement("card");
                    card.setAttribute("count", String.valueOf(item.getCount()));
                    card.setAttribute("blueprintId", blueprintId);
                    collectionTypeElem.appendChild(card);
                } else {
                    Element pack = doc.createElement("pack");
                    pack.setAttribute("count", String.valueOf(item.getCount()));
                    pack.setAttribute("blueprintId", blueprintId);
                    collectionTypeElem.appendChild(pack);
                }
            }
            deliveryElem.appendChild(collectionTypeElem);
        }

        doc.appendChild(deliveryElem);

        return doc;
    }
}
