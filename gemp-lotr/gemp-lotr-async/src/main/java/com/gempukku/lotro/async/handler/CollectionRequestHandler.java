package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.game.packs.SetDefinition;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.packs.PacksStorage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private Map<String, SetDefinition> _setDefinitions;
    private LeagueService _leagueService;
    private CollectionsManager _collectionsManager;
    private PacksStorage _packStorage;
    private LotroCardBlueprintLibrary _library;
    private LotroFormatLibrary _formatLibrary;
    private SortAndFilterCards _sortAndFilterCards;

    public CollectionRequestHandler(Map<Type, Object> context) {
        super(context);
        _setDefinitions = extractObject(context, CardSets.class).getSetDefinitions();

        _leagueService = extractObject(context, LeagueService.class);
        _collectionsManager = extractObject(context, CollectionsManager.class);
        _packStorage = extractObject(context, PacksStorage.class);
        _library = extractObject(context, LotroCardBlueprintLibrary.class);
        _formatLibrary = extractObject(context, LotroFormatLibrary.class);
        _sortAndFilterCards = new SortAndFilterCards();
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.equals("") && request.getMethod() == HttpMethod.GET) {
            getCollectionTypes(request, responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.POST) {
            openPack(request, uri.substring(1), responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.GET) {
            getCollection(request, uri.substring(1), responseWriter);
        } else {
            throw new HttpProcessingException(404);
        }
    }

    private void getCollection(HttpRequest request, String collectionType, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");
        String filter = getQueryParameterSafely(queryDecoder, "filter");
        int start = Integer.parseInt(getQueryParameterSafely(queryDecoder, "start"));
        int count = Integer.parseInt(getQueryParameterSafely(queryDecoder, "count"));

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        CardCollection collection = constructCollection(resourceOwner, collectionType);

        if (collection == null)
            throw new HttpProcessingException(404);

        Iterable<CardCollection.Item> items = collection.getAll();
        List<CardCollection.Item> filteredResult = _sortAndFilterCards.process(filter, items, _library, _formatLibrary, _setDefinitions);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionElem = doc.createElement("collection");
        collectionElem.setAttribute("count", String.valueOf(filteredResult.size()));
        doc.appendChild(collectionElem);

        for (int i = start; i < start + count; i++) {
            if (i >= 0 && i < filteredResult.size()) {
                CardCollection.Item item = filteredResult.get(i);
                String blueprintId = item.getBlueprintId();
                if (item.getType() == CardCollection.Item.Type.CARD) {
                    Element card = doc.createElement("card");
                    card.setAttribute("count", String.valueOf(item.getCount()));
                    card.setAttribute("blueprintId", blueprintId);
                    LotroCardBlueprint blueprint = _library.getLotroCardBlueprint(blueprintId);
                    appendCardSide(card, blueprint);
                    appendCardGroup(card, blueprint);
                    collectionElem.appendChild(card);
                } else {
                    Element pack = doc.createElement("pack");
                    pack.setAttribute("count", String.valueOf(item.getCount()));
                    pack.setAttribute("blueprintId", blueprintId);
                    if (item.getType() == CardCollection.Item.Type.SELECTION) {
                        List<CardCollection.Item> contents = _packStorage.openPack(blueprintId);
                        StringBuilder contentsStr = new StringBuilder();
                        for (CardCollection.Item content : contents)
                            contentsStr.append(content.getBlueprintId()).append("|");
                        contentsStr.delete(contentsStr.length() - 1, contentsStr.length());
                        pack.setAttribute("contents", contentsStr.toString());
                    }
                    collectionElem.appendChild(pack);
                }
            }
        }

        Map<String, String> headers = new HashMap<String, String>();
        processDeliveryServiceNotification(request, headers);

        responseWriter.writeXmlResponse(doc, headers);
    }

    private CardCollection constructCollection(Player player, String collectionType) {
        return _collectionsManager.getPlayerCollection(player, collectionType);
    }

    private void openPack(HttpRequest request, String collectionType, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String selection = getFormParameterSafely(postDecoder, "selection");
        String packId = getFormParameterSafely(postDecoder, "pack");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        CollectionType collectionTypeObj = createCollectionType(collectionType);
        CardCollection packContents = _collectionsManager.openPackInPlayerCollection(resourceOwner, collectionTypeObj, selection, _packStorage, packId);

        if (packContents == null)
            throw new HttpProcessingException(404);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionElem = doc.createElement("pack");
        doc.appendChild(collectionElem);

        for (CardCollection.Item item : packContents.getAll()) {
            String blueprintId = item.getBlueprintId();
            if (item.getType() == CardCollection.Item.Type.CARD) {
                Element card = doc.createElement("card");
                card.setAttribute("count", String.valueOf(item.getCount()));
                card.setAttribute("blueprintId", blueprintId);
                appendCardSide(card, _library.getLotroCardBlueprint(blueprintId));
                collectionElem.appendChild(card);
            } else {
                Element pack = doc.createElement("pack");
                pack.setAttribute("count", String.valueOf(item.getCount()));
                pack.setAttribute("blueprintId", blueprintId);
                collectionElem.appendChild(pack);
            }
        }

        responseWriter.writeXmlResponse(doc);
    }

    private void getCollectionTypes(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Element collectionsElem = doc.createElement("collections");

        for (League league : _leagueService.getActiveLeagues()) {
            LeagueSerieData serie = _leagueService.getCurrentLeagueSerie(league);
            if (serie != null && serie.isLimited() && _leagueService.isPlayerInLeague(league, resourceOwner)) {
                CollectionType collectionType = serie.getCollectionType();
                Element collectionElem = doc.createElement("collection");
                collectionElem.setAttribute("type", collectionType.getCode());
                collectionElem.setAttribute("name", collectionType.getFullName());
                collectionsElem.appendChild(collectionElem);
            }
        }

        doc.appendChild(collectionsElem);

        responseWriter.writeXmlResponse(doc);
    }

    private CollectionType createCollectionType(String collectionType) {
        final CollectionType result = CollectionType.getCollectionTypeByCode(collectionType);
        if (result != null)
            return result;

        return _leagueService.getCollectionTypeByCode(collectionType);
    }

    private void appendCardSide(Element card, LotroCardBlueprint blueprint) {
        Side side = blueprint.getSide();
        if (side != null)
            card.setAttribute("side", side.toString());
    }

    private void appendCardGroup(Element card, LotroCardBlueprint blueprint) {
        String group;
        if (blueprint.getCardType() == CardType.THE_ONE_RING)
            group = "ring";
        else if (blueprint.getCardType() == CardType.SITE)
            group = "site";
        else if (blueprint.hasKeyword(Keyword.CAN_START_WITH_RING))
            group = "ringBearer";
        else if (blueprint.getSide() == Side.FREE_PEOPLE)
            group = "fp";
        else if (blueprint.getSide() == Side.SHADOW)
            group = "shadow";
        else
            group = null;
        if (group != null)
            card.setAttribute("group", group);
    }

}
