package com.gempukku.lotro.server;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.trade.TradeServer;
import com.gempukku.lotro.trade.TradeState;
import com.gempukku.lotro.trade.TradeStateVisitor;
import com.sun.jersey.spi.resource.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map;

@Singleton
@Path("/trade")
public class TradeResource extends AbstractResource {
    @Context
    private TradeServer _tradeServer;
    @Context
    private LotroCardBlueprintLibrary _library;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getOngoingTrade(@QueryParam("participantId") String participantId,
                                    @Context HttpServletRequest request,
                                    @Context HttpServletResponse response) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element trade = doc.createElement("trade");

        final TradeState ongoingTrade = _tradeServer.getOngoingTrade(resourceOwner);

        if (ongoingTrade != null)
            ongoingTrade.processTradeStateVisitor(resourceOwner.getName(), new OngoingTradeVisitor(doc, trade));

        doc.appendChild(trade);

        processDeliveryServiceNotification(request, response);

        return doc;
    }


    private class OngoingTradeVisitor implements TradeStateVisitor {
        private Document _doc;
        private Element _root;

        private OngoingTradeVisitor(Document doc, Element root) {
            _doc = doc;
            _root = root;
        }

        @Override
        public void processTradeState(String otherParty, CardCollection offering, CardCollection getting, boolean selfAccepted, boolean otherAccepted, int tradeState, boolean selfConfirmed, boolean otherConfirmed) {
            _root.setAttribute("otherParty", otherParty);
            if (selfAccepted)
                _root.setAttribute("selfAccepted", "true");
            if (otherAccepted)
                _root.setAttribute("otherAccepted", "true");
            if (selfAccepted && otherAccepted)
                _root.setAttribute("tradeState", String.valueOf(tradeState));
            if (selfConfirmed)
                _root.setAttribute("selfConfirmed", "true");
            if (otherConfirmed)
                _root.setAttribute("otherConfirmed", "true");

            final Element offer = _doc.createElement("offer");
            appendItems(_doc, offer, offering);
            _root.appendChild(offer);
            Element get = _doc.createElement("get");
            appendItems(_doc, get, getting);
            _root.appendChild(get);
        }

        private void appendItems(Document doc, Element elem, CardCollection collection) {
            final Map<String, Integer> items = collection.getAll();
            for (Map.Entry<String, Integer> itemCount : items.entrySet()) {
                String blueprintId = itemCount.getKey();
                if (blueprintId.contains("_")) {
                    Element card = doc.createElement("card");
                    card.setAttribute("count", String.valueOf(itemCount.getValue()));
                    card.setAttribute("blueprintId", blueprintId);
                    elem.appendChild(card);
                } else {
                    Element pack = doc.createElement("pack");
                    pack.setAttribute("count", String.valueOf(itemCount.getValue()));
                    pack.setAttribute("blueprintId", blueprintId);
                    elem.appendChild(pack);
                }
            }
        }
    }
}
