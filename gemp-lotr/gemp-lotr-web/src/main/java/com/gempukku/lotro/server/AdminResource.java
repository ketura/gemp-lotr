package com.gempukku.lotro.server;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueSerieDAO;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueService;
import com.sun.jersey.spi.resource.Singleton;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Singleton
@Path("/admin")
public class AdminResource extends AbstractResource {
    @Context
    private CollectionsManager _collectionsManager;
    @Context
    private LeagueService _leagueService;
    @Context
    private DeckDAO _deckDao;
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueSerieDAO _leagueSeasonDao;
    @Context
    private LotroCardBlueprintLibrary _library;
    @Context
    private HallServer _hallServer;

    @Path("/clearCache")
    @GET
    public String clearCache(@Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _playerDao.clearCache();
        _collectionsManager.clearDBCache();
        _deckDao.clearCache();
        _leagueDao.clearCache();
        _leagueService.clearCache();

        return "OK";
    }

    @Path("/shutdown")
    @GET
    public String shutdown(
            @QueryParam("shutdown") boolean shutdown,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _hallServer.setShutdown(shutdown);

        return "OK";
    }

    @Path("/setMotd")
    @POST
    public String setMotd(
            @FormParam("motd") String motd,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _hallServer.setMOTD(motd);

        return "OK";
    }

    @Path("/addLeague")
    @POST
    public String addLeague(
            @FormParam("name") String name,
            @FormParam("type") String type,
            @FormParam("start") int start,
            @FormParam("end") int end,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _leagueDao.addLeague(name, type, start, end);

        return "OK";
    }


    @Path("/addLeagueSeason")
    @POST
    public String addLeagueSeason(
            @FormParam("leagueType") String leagueType,
            @FormParam("type") String type,
            @FormParam("format") String format,
            @FormParam("product") String product,
            @FormParam("start") int start,
            @FormParam("end") int end,
            @FormParam("maxMatches") int maxMatches,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _leagueSeasonDao.addSerie(leagueType, type, format, product, start, end, maxMatches);

        return "OK";
    }

    @Path("/addItems")
    @POST
    public String addItems(
            @FormParam("collectionType") String collectionType,
            @FormParam("product") String product,
            @FormParam("players") String players,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        DefaultCardCollection items = new DefaultCardCollection();

        Map<String, Integer> productItems = getProductItems(product);
        for (Map.Entry<String, Integer> productItem : productItems.entrySet())
            items.addItem(productItem.getKey(), productItem.getValue());

        List<String> playerNames = getItems(players);

        for (String playerName : playerNames) {
            Player player = _playerDao.getPlayer(playerName);

            _collectionsManager.addItemsToPlayerCollection(_leagueService, player, collectionType, productItems);
        }

        return "OK";
    }

    @Path("/moveCollections")
    @POST
    public String moveCollections(
            @FormParam("collectionFrom") String collectionFrom,
            @FormParam("collectionTo") String collectionTo,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        Map<Player, CardCollection> playerCollections = _collectionsManager.getPlayersCollection(collectionFrom);
        for (Map.Entry<Player, CardCollection> playerCollection : playerCollections.entrySet()) {
            Player player = playerCollection.getKey();

            _collectionsManager.moveCollectionToCollection(player, collectionFrom, collectionTo);
        }

        return "OK";
    }

    private List<String> getItems(String values) {
        List<String> result = new LinkedList<String>();
        for (String pack : values.split("\n")) {
            String blueprint = pack.trim();
            if (blueprint.length() > 0)
                result.add(blueprint);
        }
        return result;
    }

    private Map<String, Integer> getProductItems(String values) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (String item : values.split("\n")) {
            item = item.trim();
            if (item.length() > 0) {
                final String[] itemSplit = item.split("x", 2);
                if (itemSplit.length != 2)
                    throw new RuntimeException("Unable to parse the items");
                result.put(itemSplit[1].trim(), Integer.parseInt(itemSplit[0].trim()));
            }
        }
        return result;
    }

    private void validateAdmin(HttpServletRequest request) {
        Player player = getResourceOwnerSafely(request, null);

        if (!player.getType().equals("a"))
            sendError(Response.Status.FORBIDDEN);
    }
}
