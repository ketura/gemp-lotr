package com.gempukku.lotro.server;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.DeckDAO;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.MerchantDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.*;
import com.gempukku.lotro.tournament.TournamentService;
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Singleton
@Path("/admin")
public class AdminResource extends AbstractResource {
    @Context
    private CollectionsManager _collectionsManager;
    @Context
    private LeagueService _leagueService;
    @Context
    private TournamentService _tournamentService;
    @Context
    private DeckDAO _deckDao;
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private MerchantDAO _merchantDao;
    @Context
    private LotroCardBlueprintLibrary _library;
    @Context
    private HallServer _hallServer;
    @Context
    private LotroServer _lotroServer;

    @Context
    private LotroFormatLibrary _formatLibrary;

    @Path("/clearCache")
    @GET
    public String clearCache(@Context HttpServletRequest request) throws Exception {
        validateAdmin(request);

        _playerDao.clearCache();
        _collectionsManager.clearDBCache();
        _deckDao.clearCache();
        _merchantDao.clearCache();
        _leagueService.clearCache();
        _tournamentService.clearCache();

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

    @Path("/previewSealedLeague")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document previewSealedLeague(
            @FormParam("cost") int cost,
            @FormParam("format") String format,
            @FormParam("name") String name,
            @FormParam("start") String start,
            @FormParam("serieDuration") int serieDuration,
            @FormParam("maxMatches") int maxMatches,
            @Context HttpServletRequest request) throws Exception {
        validateLeagueAdmin(request);

        String code = String.valueOf(System.currentTimeMillis());

        String parameters = format + "," + start + "," + serieDuration + "," + maxMatches + "," + code + "," + name;
        LeagueData leagueData = new NewSealedLeagueData(parameters);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        final List<LeagueSerieData> series = leagueData.getSeries();

        int end = series.get(series.size() - 1).getEnd();

        Element leagueElem = doc.createElement("league");

        leagueElem.setAttribute("name", name);
        leagueElem.setAttribute("cost", String.valueOf(cost));
        leagueElem.setAttribute("start", String.valueOf(series.get(0).getStart()));
        leagueElem.setAttribute("end", String.valueOf(end));

        for (LeagueSerieData serie : series) {
            Element serieElem = doc.createElement("serie");
            serieElem.setAttribute("type", serie.getName());
            serieElem.setAttribute("maxMatches", String.valueOf(serie.getMaxMatches()));
            serieElem.setAttribute("start", String.valueOf(serie.getStart()));
            serieElem.setAttribute("end", String.valueOf(serie.getEnd()));
            serieElem.setAttribute("format", _formatLibrary.getFormat(serie.getFormat()).getName());
            serieElem.setAttribute("collection", serie.getCollectionType().getFullName());
            serieElem.setAttribute("limited", String.valueOf(serie.isLimited()));

            leagueElem.appendChild(serieElem);
        }

        doc.appendChild(leagueElem);

        return doc;
    }

    @Path("/addSealedLeague")
    @POST
    public String addSealedLeague(
            @FormParam("cost") int cost,
            @FormParam("format") String format,
            @FormParam("name") String name,
            @FormParam("start") String start,
            @FormParam("serieDuration") int serieDuration,
            @FormParam("maxMatches") int maxMatches,
            @Context HttpServletRequest request) throws Exception {
        validateLeagueAdmin(request);

        String code = String.valueOf(System.currentTimeMillis());

        String parameters = format + "," + start + "," + serieDuration + "," + maxMatches + "," + code + "," + name;
        LeagueData leagueData = new NewSealedLeagueData(parameters);
        List<LeagueSerieData> series = leagueData.getSeries();
        int leagueStart = series.get(0).getStart();
        int displayEnd = DateUtils.offsetDate(series.get(series.size() - 1).getEnd(), 2);

        _leagueDao.addLeague(cost, name, code, leagueData.getClass().getName(), parameters, leagueStart, displayEnd);

        _leagueService.clearCache();

        return "OK";
    }

    @Path("/previewConstructedLeague")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public Document previewConstructedLeague(
            @FormParam("cost") int cost,
            @FormParam("name") String name,
            @FormParam("start") String start,
            @FormParam("prizeMultiplier") float prizeMultiplier,
            @FormParam("collectionType") String collectionType,
            @FormParam("format") List<String> formats,
            @FormParam("serieDuration") List<Integer> serieDurations,
            @FormParam("maxMatches") List<Integer> maxMatches,
            @Context HttpServletRequest request) throws Exception {
        validateLeagueAdmin(request);

        StringBuilder sb = new StringBuilder();
        sb.append(start + "," + collectionType + "," + prizeMultiplier + "," + formats.size());
        for (int i = 0; i < formats.size(); i++)
            sb.append("," + formats.get(i) + "," + serieDurations.get(i) + "," + maxMatches.get(i));

        String parameters = sb.toString();
        LeagueData leagueData = new NewConstructedLeagueData(parameters);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        final List<LeagueSerieData> series = leagueData.getSeries();

        int end = series.get(series.size() - 1).getEnd();

        Element leagueElem = doc.createElement("league");

        leagueElem.setAttribute("name", name);
        leagueElem.setAttribute("cost", String.valueOf(cost));
        leagueElem.setAttribute("start", String.valueOf(series.get(0).getStart()));
        leagueElem.setAttribute("end", String.valueOf(end));

        for (LeagueSerieData serie : series) {
            Element serieElem = doc.createElement("serie");
            serieElem.setAttribute("type", serie.getName());
            serieElem.setAttribute("maxMatches", String.valueOf(serie.getMaxMatches()));
            serieElem.setAttribute("start", String.valueOf(serie.getStart()));
            serieElem.setAttribute("end", String.valueOf(serie.getEnd()));
            serieElem.setAttribute("format", _formatLibrary.getFormat(serie.getFormat()).getName());
            serieElem.setAttribute("collection", serie.getCollectionType().getFullName());
            serieElem.setAttribute("limited", String.valueOf(serie.isLimited()));

            leagueElem.appendChild(serieElem);
        }

        doc.appendChild(leagueElem);

        return doc;
    }

    @Path("/addConstructedLeague")
    @POST
    public String addConstructedLeague(
            @FormParam("cost") int cost,
            @FormParam("name") String name,
            @FormParam("start") String start,
            @FormParam("prizeMultiplier") float prizeMultiplier,
            @FormParam("collectionType") String collectionType,
            @FormParam("format") List<String> formats,
            @FormParam("serieDuration") List<Integer> serieDurations,
            @FormParam("maxMatches") List<Integer> maxMatches,
            @Context HttpServletRequest request) throws Exception {
        validateLeagueAdmin(request);

        String code = String.valueOf(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();
        sb.append(start + "," + collectionType + "," + prizeMultiplier + "," + formats.size());
        for (int i = 0; i < formats.size(); i++)
            sb.append("," + formats.get(i) + "," + serieDurations.get(i) + "," + maxMatches.get(i));

        String parameters = sb.toString();
        LeagueData leagueData = new NewConstructedLeagueData(parameters);
        List<LeagueSerieData> series = leagueData.getSeries();
        int leagueStart = series.get(0).getStart();
        int displayEnd = DateUtils.offsetDate(series.get(series.size() - 1).getEnd(), 2);

        _leagueDao.addLeague(cost, name, code, leagueData.getClass().getName(), parameters, leagueStart, displayEnd);

        _leagueService.clearCache();

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

        Collection<CardCollection.Item> productItems = getProductItems(product);

        List<String> playerNames = getItems(players);

        for (String playerName : playerNames) {
            Player player = _playerDao.getPlayer(playerName);

            _collectionsManager.addItemsToPlayerCollection(true, "Administrator action", player, createCollectionType(collectionType), productItems);
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

    private Collection<CardCollection.Item> getProductItems(String values) {
        List<CardCollection.Item> result = new LinkedList<CardCollection.Item>();
        for (String item : values.split("\n")) {
            item = item.trim();
            if (item.length() > 0) {
                final String[] itemSplit = item.split("x", 2);
                if (itemSplit.length != 2)
                    throw new RuntimeException("Unable to parse the items");
                result.add(CardCollection.Item.createItem(itemSplit[1].trim(), Integer.parseInt(itemSplit[0].trim())));
            }
        }
        return result;
    }

    private void validateAdmin(HttpServletRequest request) {
        Player player = getResourceOwnerSafely(request, null);

        if (!player.getType().contains("a"))
            sendError(Response.Status.FORBIDDEN);
    }

    private void validateLeagueAdmin(HttpServletRequest request) {
        Player player = getResourceOwnerSafely(request, null);

        if (!player.getType().contains("l"))
            sendError(Response.Status.FORBIDDEN);
    }

    private CollectionType createCollectionType(String collectionType) {
        if (collectionType.equals("permanent"))
            return new CollectionType("permanent", "My cards");

        return _leagueService.getCollectionTypeByCode(collectionType);
    }
}
