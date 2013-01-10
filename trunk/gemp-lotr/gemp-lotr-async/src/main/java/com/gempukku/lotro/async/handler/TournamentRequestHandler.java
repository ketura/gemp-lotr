package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.SortAndFilterCards;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.vo.LotroDeck;
import com.gempukku.lotro.tournament.Tournament;
import com.gempukku.lotro.tournament.TournamentService;
import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class TournamentRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private TournamentService _tournamentService;
    private LotroFormatLibrary _formatLibrary;
    private LotroCardBlueprintLibrary _library;
    private SortAndFilterCards _sortAndFilterCards;

    public TournamentRequestHandler(Map<Type, Object> context) {
        super(context);

        _tournamentService = extractObject(context, TournamentService.class);
        _formatLibrary = extractObject(context, LotroFormatLibrary.class);
        _library = extractObject(context, LotroCardBlueprintLibrary.class);
        _sortAndFilterCards = new SortAndFilterCards();
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, MessageEvent e) throws Exception {
        if (uri.equals("") && request.getMethod() == HttpMethod.GET) {
            getCurrentTournaments(request, responseWriter);
        } else if (uri.equals("/history") && request.getMethod() == HttpMethod.GET) {
            getTournamentHistory(request, responseWriter);
        } else if (uri.startsWith("/") && uri.endsWith("/html") && uri.contains("/deck/") && request.getMethod() == HttpMethod.GET) {
            getTournamentDeck(request, uri.substring(1, uri.indexOf("/deck/")), uri.substring(uri.indexOf("/deck/") + 6, uri.lastIndexOf("/html")), responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.GET) {
            getTournamentInfo(request, uri.substring(1), responseWriter);
        } else {
            responseWriter.writeError(404);
        }
    }

    private void getTournamentInfo(HttpRequest request, String tournamentId, ResponseWriter responseWriter) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();

        Tournament tournament = _tournamentService.getTournamentById(tournamentId);
        if (tournament == null)
            throw new HttpProcessingException(404);

        Element tournamentElem = doc.createElement("tournament");

        tournamentElem.setAttribute("id", tournament.getTournamentId());
        tournamentElem.setAttribute("name", tournament.getTournamentName());
        tournamentElem.setAttribute("format", _formatLibrary.getFormat(tournament.getFormat()).getName());
        tournamentElem.setAttribute("collection", tournament.getCollectionType().getFullName());
        tournamentElem.setAttribute("round", String.valueOf(tournament.getCurrentRound()));
        tournamentElem.setAttribute("stage", tournament.getTournamentStage().getHumanReadable());

        List<PlayerStanding> leagueStandings = tournament.getCurrentStandings();
        for (PlayerStanding standing : leagueStandings) {
            Element standingElem = doc.createElement("tournamentStanding");
            setStandingAttributes(standing, standingElem);
            tournamentElem.appendChild(standingElem);
        }

        doc.appendChild(tournamentElem);

        responseWriter.writeXmlResponse(doc);
    }

    private void setStandingAttributes(PlayerStanding standing, Element standingElem) {
        standingElem.setAttribute("player", standing.getPlayerName());
        standingElem.setAttribute("standing", String.valueOf(standing.getStanding()));
        standingElem.setAttribute("points", String.valueOf(standing.getPoints()));
        standingElem.setAttribute("gamesPlayed", String.valueOf(standing.getGamesPlayed()));
        DecimalFormat format = new DecimalFormat("##0.00%");
        standingElem.setAttribute("opponentWin", format.format(standing.getOpponentWin()));
    }

    private void getTournamentDeck(HttpRequest request, String tournamentId, String playerName, ResponseWriter responseWriter) throws Exception {
        Tournament tournament = _tournamentService.getTournamentById(tournamentId);
        if (tournament == null)
            throw new HttpProcessingException(404);

        if (tournament.getTournamentStage() != Tournament.Stage.FINISHED)
            throw new HttpProcessingException(403);

        LotroDeck deck = _tournamentService.getPlayerDeck(tournamentId, playerName);
        if (deck == null)
            throw new HttpProcessingException(404);

        StringBuilder result = new StringBuilder();
        result.append("<html><body>");
        result.append("<h1>" + StringEscapeUtils.escapeHtml(deck.getDeckName()) + "</h1>");
        result.append("<h2>by " + playerName + "</h2>");
        String ringBearer = deck.getRingBearer();
        if (ringBearer != null)
            result.append("<b>Ring-bearer:</b> " + GameUtils.getFullName(_library.getLotroCardBlueprint(ringBearer)) + "<br/>");
        String ring = deck.getRing();
        if (ring != null)
            result.append("<b>Ring:</b> " + GameUtils.getFullName(_library.getLotroCardBlueprint(ring)) + "<br/>");

        DefaultCardCollection deckCards = new DefaultCardCollection();
        for (String card : deck.getAdventureCards())
            deckCards.addItem(_library.getBaseBlueprintId(card), 1);
        for (String site : deck.getSites())
            deckCards.addItem(_library.getBaseBlueprintId(site), 1);

        result.append("<br/>");
        result.append("<b>Adventure deck:</b><br/>");
        for (CardCollection.Item item : _sortAndFilterCards.process("cardType:SITE sort:siteNumber,twilight", deckCards.getAll().values(), _library, _formatLibrary, null))
            result.append(GameUtils.getFullName(_library.getLotroCardBlueprint(item.getBlueprintId())) + "<br/>");

        result.append("<br/>");
        result.append("<b>Free Peoples Draw Deck:</b><br/>");
        for (CardCollection.Item item : _sortAndFilterCards.process("side:FREE_PEOPLE sort:cardType,culture,name", deckCards.getAll().values(), _library, _formatLibrary, null))
            result.append(item.getCount() + "x " + GameUtils.getFullName(_library.getLotroCardBlueprint(item.getBlueprintId())) + "<br/>");

        result.append("<br/>");
        result.append("<b>Shadow Draw Deck:</b><br/>");
        for (CardCollection.Item item : _sortAndFilterCards.process("side:SHADOW sort:cardType,culture,name", deckCards.getAll().values(), _library, _formatLibrary, null))
            result.append(item.getCount() + "x " + GameUtils.getFullName(_library.getLotroCardBlueprint(item.getBlueprintId())) + "<br/>");

        result.append("</body></html>");

        responseWriter.writeHtmlResponse(result.toString());
    }

    private void getTournamentHistory(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element tournaments = doc.createElement("tournaments");

        for (Tournament tournament : _tournamentService.getOldTournaments(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7))) {
            Element tournamentElem = doc.createElement("tournament");

            tournamentElem.setAttribute("id", tournament.getTournamentId());
            tournamentElem.setAttribute("name", tournament.getTournamentName());
            tournamentElem.setAttribute("format", _formatLibrary.getFormat(tournament.getFormat()).getName());
            tournamentElem.setAttribute("collection", tournament.getCollectionType().getFullName());
            tournamentElem.setAttribute("round", String.valueOf(tournament.getCurrentRound()));
            tournamentElem.setAttribute("stage", tournament.getTournamentStage().getHumanReadable());

            tournaments.appendChild(tournamentElem);
        }

        doc.appendChild(tournaments);

        responseWriter.writeXmlResponse(doc);
    }

    private void getCurrentTournaments(HttpRequest request, ResponseWriter responseWriter) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element tournaments = doc.createElement("tournaments");

        for (Tournament tournament : _tournamentService.getLiveTournaments()) {
            Element tournamentElem = doc.createElement("tournament");

            tournamentElem.setAttribute("id", tournament.getTournamentId());
            tournamentElem.setAttribute("name", tournament.getTournamentName());
            tournamentElem.setAttribute("format", _formatLibrary.getFormat(tournament.getFormat()).getName());
            tournamentElem.setAttribute("collection", tournament.getCollectionType().getFullName());
            tournamentElem.setAttribute("round", String.valueOf(tournament.getCurrentRound()));
            tournamentElem.setAttribute("stage", tournament.getTournamentStage().getHumanReadable());

            tournaments.appendChild(tournamentElem);
        }

        doc.appendChild(tournaments);

        responseWriter.writeXmlResponse(doc);
    }
}
