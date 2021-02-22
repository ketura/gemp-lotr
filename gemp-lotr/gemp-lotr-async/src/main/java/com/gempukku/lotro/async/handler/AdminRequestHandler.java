package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.cache.CacheManager;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.common.ApplicationConfiguration;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.draft2.SoloDraftDefinitions;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.CardSets;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.*;
import com.gempukku.lotro.service.AdminService;
import com.gempukku.lotro.tournament.TournamentService;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdminRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private final LotroCardBlueprintLibrary lotroCardBlueprintLibrary;
    private SoloDraftDefinitions _soloDraftDefinitions;
    private LeagueService _leagueService;
    private TournamentService _tournamentService;
    private CacheManager _cacheManager;
    private HallServer _hallServer;
    private LotroFormatLibrary _formatLibrary;
    private LeagueDAO _leagueDao;
    private CollectionsManager _collectionManager;
    private CardSets _cardSets;
    private PlayerDAO _playerDAO;
    private AdminService _adminService;

    public AdminRequestHandler(Map<Type, Object> context) {
        super(context);
        _cardSets = extractObject(context, CardSets.class);
        _soloDraftDefinitions = extractObject(context, SoloDraftDefinitions.class);
        _leagueService = extractObject(context, LeagueService.class);
        _tournamentService = extractObject(context, TournamentService.class);
        _cacheManager = extractObject(context, CacheManager.class);
        _hallServer = extractObject(context, HallServer.class);
        _formatLibrary = extractObject(context, LotroFormatLibrary.class);
        _leagueDao = extractObject(context, LeagueDAO.class);
        _playerDAO = extractObject(context, PlayerDAO.class);
        _collectionManager = extractObject(context, CollectionsManager.class);
        _adminService = extractObject(context, AdminService.class);
        lotroCardBlueprintLibrary = extractObject(context, LotroCardBlueprintLibrary.class);
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.equals("/clearCache") && request.getMethod() == HttpMethod.GET) {
            clearCache(request, responseWriter);
        } else if (uri.equals("/shutdown") && request.getMethod() == HttpMethod.GET) {
            shutdown(request, responseWriter);
        } else if (uri.equals("/reloadCards") && request.getMethod() == HttpMethod.GET) {
            reloadCards(request, responseWriter);
        } else if (uri.equals("/setMotd") && request.getMethod() == HttpMethod.POST) {
            setMotd(request, responseWriter);
        } else if (uri.equals("/previewSealedLeague") && request.getMethod() == HttpMethod.POST) {
            previewSealedLeague(request, responseWriter);
        } else if (uri.equals("/addSealedLeague") && request.getMethod() == HttpMethod.POST) {
            addSealedLeague(request, responseWriter);
        } else if (uri.equals("/previewConstructedLeague") && request.getMethod() == HttpMethod.POST) {
            previewConstructedLeague(request, responseWriter);
        } else if (uri.equals("/addConstructedLeague") && request.getMethod() == HttpMethod.POST) {
            addConstructedLeague(request, responseWriter);
        } else if (uri.equals("/previewSoloDraftLeague") && request.getMethod() == HttpMethod.POST) {
            previewSoloDraftLeague(request, responseWriter);
        } else if (uri.equals("/addSoloDraftLeague") && request.getMethod() == HttpMethod.POST) {
            addSoloDraftLeague(request, responseWriter);
        } else if (uri.equals("/addItems") && request.getMethod() == HttpMethod.POST) {
            addItems(request, responseWriter);
        } else if (uri.equals("/addItemsToCollection") && request.getMethod() == HttpMethod.POST) {
            addItemsToCollection(request, responseWriter);
        } else if (uri.equals("/banUser") && request.getMethod() == HttpMethod.POST) {
            banUser(request, responseWriter);
        } else if (uri.equals("/banMultiple") && request.getMethod() == HttpMethod.POST) {
            banMultiple(request, responseWriter);
        } else if (uri.equals("/banUserTemp") && request.getMethod() == HttpMethod.POST) {
            banUserTemp(request, responseWriter);
        } else if (uri.equals("/unBanUser") && request.getMethod() == HttpMethod.POST) {
            unBanUser(request, responseWriter);
        } else if (uri.equals("/findMultipleAccounts") && request.getMethod() == HttpMethod.POST) {
            findMultipleAccounts(request, responseWriter);
        } else {
            throw new HttpProcessingException(404);
        }
    }

    private void findMultipleAccounts(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
            String login = getFormParameterSafely(postDecoder, "login");

            List<Player> similarPlayers = _playerDAO.findSimilarAccounts(login);
            if (similarPlayers == null)
                throw new HttpProcessingException(404);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();
            Element players = doc.createElement("players");

            for (Player similarPlayer : similarPlayers) {
                Element playerElem = doc.createElement("player");
                playerElem.setAttribute("id", String.valueOf(similarPlayer.getId()));
                playerElem.setAttribute("name", similarPlayer.getName());
                playerElem.setAttribute("password", similarPlayer.getPassword());
                playerElem.setAttribute("status", getStatus(similarPlayer));
                playerElem.setAttribute("createIp", similarPlayer.getCreateIp());
                playerElem.setAttribute("loginIp", similarPlayer.getLastIp());
                players.appendChild(playerElem);
            }

            doc.appendChild(players);

            responseWriter.writeXmlResponse(doc);
        } finally {
            postDecoder.destroy();
        }
    }

    private String getStatus(Player similarPlayer) {
        if (similarPlayer.getType().equals(""))
            return "Banned permanently";
        if (similarPlayer.getBannedUntil() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return "Banned until " + format.format(similarPlayer.getBannedUntil());
        }
        if (similarPlayer.getType().contains("n"))
            return "Unbanned";
        return "OK";
    }

    private void banUser(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String login = getFormParameterSafely(postDecoder, "login");

        if (login==null)
            throw new HttpProcessingException(404);

        if (!_adminService.banUser(login))
            throw new HttpProcessingException(404);

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void banMultiple(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        List<String> logins = getFormParametersSafely(postDecoder, "login");
        if (logins == null)
            throw new HttpProcessingException(404);

        for (String login : logins) {
            if (!_adminService.banUser(login))
                throw new HttpProcessingException(404);
        }

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void banUserTemp(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String login = getFormParameterSafely(postDecoder, "login");
        int duration = Integer.parseInt(getFormParameterSafely(postDecoder, "duration"));

        if (!_adminService.banUserTemp(login, duration))
            throw new HttpProcessingException(404);

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void unBanUser(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String login = getFormParameterSafely(postDecoder, "login");

        if (!_adminService.unBanUser(login))
            throw new HttpProcessingException(404);

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void addItemsToCollection(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String reason = getFormParameterSafely(postDecoder, "reason");
        String product = getFormParameterSafely(postDecoder, "product");
        String collectionType = getFormParameterSafely(postDecoder, "collectionType");

        Collection<CardCollection.Item> productItems = getProductItems(product);

        Map<Player, CardCollection> playersCollection = _collectionManager.getPlayersCollection(collectionType);

        for (Map.Entry<Player, CardCollection> playerCollection : playersCollection.entrySet())
            _collectionManager.addItemsToPlayerCollection(true, reason, playerCollection.getKey(), createCollectionType(collectionType), productItems);

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void addItems(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String players = getFormParameterSafely(postDecoder, "players");
        String product = getFormParameterSafely(postDecoder, "product");
        String collectionType = getFormParameterSafely(postDecoder, "collectionType");

        Collection<CardCollection.Item> productItems = getProductItems(product);

        List<String> playerNames = getItems(players);

        for (String playerName : playerNames) {
            Player player = _playerDao.getPlayer(playerName);

            _collectionManager.addItemsToPlayerCollection(true, "Administrator action", player, createCollectionType(collectionType), productItems);
        }

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
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

    private CollectionType createCollectionType(String collectionType) {
        final CollectionType result = CollectionType.getCollectionTypeByCode(collectionType);
        if (result != null)
            return result;

        return _leagueService.getCollectionTypeByCode(collectionType);
    }

    private void addConstructedLeague(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateLeagueAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String start = getFormParameterSafely(postDecoder, "start");
        String collectionType = getFormParameterSafely(postDecoder, "collectionType");
        String prizeMultiplier = getFormParameterSafely(postDecoder, "prizeMultiplier");
        List<String> formats = getFormMultipleParametersSafely(postDecoder, "format");
        List<String> serieDurations = getFormMultipleParametersSafely(postDecoder, "serieDuration");
        List<String> maxMatches = getFormMultipleParametersSafely(postDecoder, "maxMatches");
        String name = getFormParameterSafely(postDecoder, "name");
        int cost = Integer.parseInt(getFormParameterSafely(postDecoder, "cost"));

        String code = String.valueOf(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();
        sb.append(start + "," + collectionType + "," + prizeMultiplier + "," + formats.size());
        for (int i = 0; i < formats.size(); i++)
            sb.append("," + formats.get(i) + "," + serieDurations.get(i) + "," + maxMatches.get(i));

        String parameters = sb.toString();
        LeagueData leagueData = new NewConstructedLeagueData(_cardSets, _soloDraftDefinitions, parameters);
        List<LeagueSerieData> series = leagueData.getSeries();
        int leagueStart = series.get(0).getStart();
        int displayEnd = DateUtils.offsetDate(series.get(series.size() - 1).getEnd(), 2);

        _leagueDao.addLeague(cost, name, code, leagueData.getClass().getName(), parameters, leagueStart, displayEnd);

        _leagueService.clearCache();

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void previewConstructedLeague(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateLeagueAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String start = getFormParameterSafely(postDecoder, "start");
        String collectionType = getFormParameterSafely(postDecoder, "collectionType");
        String prizeMultiplier = getFormParameterSafely(postDecoder, "prizeMultiplier");
        List<String> formats = getFormMultipleParametersSafely(postDecoder, "format");
        List<String> serieDurations = getFormMultipleParametersSafely(postDecoder, "serieDuration");
        List<String> maxMatches = getFormMultipleParametersSafely(postDecoder, "maxMatches");
        String name = getFormParameterSafely(postDecoder, "name");
        int cost = Integer.parseInt(getFormParameterSafely(postDecoder, "cost"));

        StringBuilder sb = new StringBuilder();
        sb.append(start + "," + collectionType + "," + prizeMultiplier + "," + formats.size());
        for (int i = 0; i < formats.size(); i++)
            sb.append("," + formats.get(i) + "," + serieDurations.get(i) + "," + maxMatches.get(i));

        String parameters = sb.toString();
        LeagueData leagueData = new NewConstructedLeagueData(_cardSets, _soloDraftDefinitions, parameters);

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

        responseWriter.writeXmlResponse(doc);
        } finally {
            postDecoder.destroy();
        }
    }

    private void addSoloDraftLeague(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateLeagueAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String format = getFormParameterSafely(postDecoder, "format");
        String start = getFormParameterSafely(postDecoder, "start");
        String serieDuration = getFormParameterSafely(postDecoder, "serieDuration");
        String maxMatches = getFormParameterSafely(postDecoder, "maxMatches");
        String name = getFormParameterSafely(postDecoder, "name");
        int cost = Integer.parseInt(getFormParameterSafely(postDecoder, "cost"));

        String code = String.valueOf(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();
        sb.append(format+","+start+","+serieDuration+","+maxMatches+","+code+","+name);

        String parameters = sb.toString();
        LeagueData leagueData = new SoloDraftLeagueData(_cardSets, _soloDraftDefinitions, parameters);
        List<LeagueSerieData> series = leagueData.getSeries();
        int leagueStart = series.get(0).getStart();
        int displayEnd = DateUtils.offsetDate(series.get(series.size() - 1).getEnd(), 2);

        _leagueDao.addLeague(cost, name, code, leagueData.getClass().getName(), parameters, leagueStart, displayEnd);

        _leagueService.clearCache();

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void previewSoloDraftLeague(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateLeagueAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String format = getFormParameterSafely(postDecoder, "format");
        String start = getFormParameterSafely(postDecoder, "start");
        String serieDuration = getFormParameterSafely(postDecoder, "serieDuration");
        String maxMatches = getFormParameterSafely(postDecoder, "maxMatches");
        String name = getFormParameterSafely(postDecoder, "name");
        int cost = Integer.parseInt(getFormParameterSafely(postDecoder, "cost"));

        String code = String.valueOf(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();
        sb.append(format+","+start+","+serieDuration+","+maxMatches+","+code+","+name);

        String parameters = sb.toString();
        LeagueData leagueData = new SoloDraftLeagueData(_cardSets, _soloDraftDefinitions, parameters);

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

        responseWriter.writeXmlResponse(doc);
        } finally {
            postDecoder.destroy();
        }
    }

    private void addSealedLeague(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateLeagueAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String format = getFormParameterSafely(postDecoder, "format");
        String start = getFormParameterSafely(postDecoder, "start");
        String serieDuration = getFormParameterSafely(postDecoder, "serieDuration");
        String maxMatches = getFormParameterSafely(postDecoder, "maxMatches");
        String name = getFormParameterSafely(postDecoder, "name");
        int cost = Integer.parseInt(getFormParameterSafely(postDecoder, "cost"));

        String code = String.valueOf(System.currentTimeMillis());

        String parameters = format + "," + start + "," + serieDuration + "," + maxMatches + "," + code + "," + name;
        LeagueData leagueData = new NewSealedLeagueData(_cardSets, _soloDraftDefinitions, parameters);
        List<LeagueSerieData> series = leagueData.getSeries();
        int leagueStart = series.get(0).getStart();
        int displayEnd = DateUtils.offsetDate(series.get(series.size() - 1).getEnd(), 2);

        _leagueDao.addLeague(cost, name, code, leagueData.getClass().getName(), parameters, leagueStart, displayEnd);

        _leagueService.clearCache();

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void previewSealedLeague(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        validateLeagueAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String format = getFormParameterSafely(postDecoder, "format");
        String start = getFormParameterSafely(postDecoder, "start");
        String serieDuration = getFormParameterSafely(postDecoder, "serieDuration");
        String maxMatches = getFormParameterSafely(postDecoder, "maxMatches");
        String name = getFormParameterSafely(postDecoder, "name");
        int cost = Integer.parseInt(getFormParameterSafely(postDecoder, "cost"));

        String code = String.valueOf(System.currentTimeMillis());

        String parameters = format + "," + start + "," + serieDuration + "," + maxMatches + "," + code + "," + name;
        LeagueData leagueData = new NewSealedLeagueData(_cardSets, _soloDraftDefinitions, parameters);

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

        responseWriter.writeXmlResponse(doc);
        } finally {
            postDecoder.destroy();
        }
    }

    private void setMotd(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException, Exception {
        validateAdmin(request);

        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
        String motd = getFormParameterSafely(postDecoder, "motd");

        _hallServer.setMOTD(motd);

        responseWriter.writeHtmlResponse("OK");
        } finally {
            postDecoder.destroy();
        }
    }

    private void shutdown(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException {
        validateAdmin(request);

        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        boolean shutdown = Boolean.valueOf(getQueryParameterSafely(queryDecoder, "shutdown"));

        _hallServer.setShutdown(shutdown);

        responseWriter.writeHtmlResponse("OK");
    }

    private void reloadCards(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException {
        validateAdmin(request);

        lotroCardBlueprintLibrary.reloadCards(new java.io.File(ApplicationConfiguration.getProperty("card.path")));

        responseWriter.writeHtmlResponse("OK");
    }

    private void clearCache(HttpRequest request, ResponseWriter responseWriter) throws HttpProcessingException {
        validateAdmin(request);

        _leagueService.clearCache();
        _tournamentService.clearCache();

        int before = _cacheManager.getTotalCount();

        _cacheManager.clearCaches();

        int after = _cacheManager.getTotalCount();

        responseWriter.writeHtmlResponse("Before: " + before + "<br>OK<br>After: " + after);
    }

    private void validateAdmin(HttpRequest request) throws HttpProcessingException {
        Player player = getResourceOwnerSafely(request, null);

        if (!player.getType().contains("a"))
            throw new HttpProcessingException(403);
    }

    private void validateLeagueAdmin(HttpRequest request) throws HttpProcessingException {
        Player player = getResourceOwnerSafely(request, null);

        if (!player.getType().contains("l"))
            throw new HttpProcessingException(403);
    }
}
