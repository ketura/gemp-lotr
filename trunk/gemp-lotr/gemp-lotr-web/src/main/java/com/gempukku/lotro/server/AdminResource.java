package com.gempukku.lotro.server;

import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueSeasonDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.MutableCardCollection;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.hall.HallServer;
import com.sun.jersey.spi.resource.Singleton;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Singleton
@Path("/admin")
public class AdminResource extends AbstractResource {
    @Context
    private CollectionDAO _collectionDao;
    @Context
    private DeckDAO _deckDao;
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueSeasonDAO _leagueSeasonDao;
    @Context
    private LotroCardBlueprintLibrary _library;
    @Context
    private HallServer _hallServer;

    @Path("/clearCache")
    @GET
    public String clearCache(@Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _playerDao.clearCache();
        _collectionDao.clearCache();
        _deckDao.clearCache();
        _leagueDao.clearCache();

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
            @FormParam("packCollection") String packCollection,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        DefaultCardCollection collection = new DefaultCardCollection();
        List<String> packs = getPacks(packCollection);
        for (String pack : packs)
            collection.addPacks(pack, 1);
        collection.finishedReading();

        _leagueDao.addLeague(name, type, collection, start, end);

        return "OK";
    }


    @Path("/addLeagueSeason")
    @POST
    public String addLeagueSeason(
            @FormParam("leagueType") String leagueType,
            @FormParam("type") String type,
            @FormParam("start") int start,
            @FormParam("end") int end,
            @FormParam("maxMatches") int maxMatches,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _leagueSeasonDao.addSeason(leagueType, type, start, end, maxMatches);

        return "OK";
    }

    @Path("/addLeagueProduct")
    @POST
    public String addLeagueProduct(
            @FormParam("leagueType") String leagueType,
            @FormParam("packProduct") String packProduct,
            @Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        League league = getLeagueByType(leagueType);
        if (league == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);

        MutableCardCollection baseCollection = league.getBaseCollection();
        List<String> packs = getPacks(packProduct);
        for (String pack : packs)
            baseCollection.addPacks(pack, 1);
        _leagueDao.setBaseCollectionForLeague(league, baseCollection);

        Map<Integer, MutableCardCollection> playerCollections = _collectionDao.getPlayerCollectionsByType(leagueType);
        for (Map.Entry<Integer, MutableCardCollection> playerCollection : playerCollections.entrySet()) {
            int playerId = playerCollection.getKey();
            MutableCardCollection collection = playerCollection.getValue();
            for (String pack : packs)
                collection.addPacks(pack, 1);
            Player player = _playerDao.getPlayer(playerId);
            _collectionDao.setCollectionForPlayer(player, leagueType, collection);
        }

        return "OK";
    }

    private List<String> getPacks(String packProduct) {
        List<String> result = new LinkedList<String>();
        for (String pack : packProduct.split("\n"))
            result.add(pack.trim());
        return result;
    }

    private League getLeagueByType(String leagueType) {
        for (League league : _leagueDao.getActiveLeagues())
            if (league.getType().equals(leagueType))
                return league;
        return null;
    }

    private void validateAdmin(HttpServletRequest request) {
        Player player = getResourceOwnerSafely(request, null);

        if (!player.getType().equals("a"))
            sendError(Response.Status.FORBIDDEN);
    }
}
