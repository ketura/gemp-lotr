package com.gempukku.lotro.server;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatchResult;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.league.LeagueData;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.sun.jersey.spi.resource.Singleton;
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
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

@Singleton
@Path("/league")
public class LeagueResource extends AbstractResource {
    @Context
    private LeagueService _leagueService;
    @Context
    private LotroFormatLibrary _formatLibrary;

    @Path("/{leagueType}")
    @POST
    public void joinLeague(
            @PathParam("leagueType") String leagueType,
            @FormParam("participantId") String participantId,
            @Context HttpServletRequest request) throws ParserConfigurationException {
        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        League league = _leagueService.getLeagueByType(leagueType);
        if (league == null)
            sendError(Response.Status.NOT_FOUND);

        if (!_leagueService.playerJoinsLeague(league, resourceOwner, request.getRemoteAddr()))
            sendError(Response.Status.CONFLICT);
    }

    @GET
    public Document getNonExpiresLeagues(
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element leagues = doc.createElement("leagues");

        for (League league : _leagueService.getActiveLeagues()) {
            final LeagueData leagueData = league.getLeagueData();
            final List<LeagueSerieData> series = leagueData.getSeries();

            int end = series.get(series.size() - 1).getEnd();

            Element leagueElem = doc.createElement("league");

            leagueElem.setAttribute("type", league.getType());
            leagueElem.setAttribute("name", league.getName());
            leagueElem.setAttribute("start", String.valueOf(series.get(0).getStart()));
            leagueElem.setAttribute("end", String.valueOf(end));

            leagues.appendChild(leagueElem);
        }

        doc.appendChild(leagues);

        return doc;
    }

    @Path("/{leagueType}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Document getLeagueInformation(
            @PathParam("leagueType") String leagueType,
            @QueryParam("participantId") String participantId,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        Document doc = documentBuilder.newDocument();

        League league = getLeagueByType(leagueType);

        if (league == null)
            sendError(Response.Status.NOT_FOUND);

        final LeagueData leagueData = league.getLeagueData();
        final List<LeagueSerieData> series = leagueData.getSeries();

        int end = series.get(series.size() - 1).getEnd();

        Element leagueElem = doc.createElement("league");
        boolean inLeague = _leagueService.isPlayerInLeague(league, resourceOwner);

        leagueElem.setAttribute("member", String.valueOf(inLeague));
        leagueElem.setAttribute("joinable", String.valueOf(!inLeague && end >= DateUtils.getCurrentDate()));
        leagueElem.setAttribute("type", league.getType());
        leagueElem.setAttribute("name", league.getName());
        leagueElem.setAttribute("cost", String.valueOf(league.getCost()));
        leagueElem.setAttribute("start", String.valueOf(series.get(0).getStart()));
        leagueElem.setAttribute("end", String.valueOf(end));

        for (LeagueSerieData serie : series) {
            Element serieElem = doc.createElement("serie");
            serieElem.setAttribute("type", serie.getName());
            serieElem.setAttribute("maxMatches", String.valueOf(serie.getMaxMatches()));
            serieElem.setAttribute("start", String.valueOf(serie.getStart()));
            serieElem.setAttribute("end", String.valueOf(serie.getEnd()));
            serieElem.setAttribute("formatType", serie.getFormat());
            serieElem.setAttribute("format", _formatLibrary.getFormat(serie.getFormat()).getName());
            serieElem.setAttribute("collection", serie.getCollectionType().getFullName());
            serieElem.setAttribute("limited", String.valueOf(serie.isLimited()));

            Element matchesElem = doc.createElement("matches");
            Collection<LeagueMatchResult> playerMatches = _leagueService.getPlayerMatchesInSerie(league, serie, resourceOwner.getName());
            for (LeagueMatchResult playerMatch : playerMatches) {
                Element matchElem = doc.createElement("match");
                matchElem.setAttribute("winner", playerMatch.getWinner());
                matchElem.setAttribute("loser", playerMatch.getLoser());
                matchesElem.appendChild(matchElem);
            }
            serieElem.appendChild(matchesElem);

            final List<PlayerStanding> standings = _leagueService.getLeagueSerieStandings(league, serie);
            for (PlayerStanding standing : standings) {
                Element standingElem = doc.createElement("standing");
                setStandingAttributes(standing, standingElem);
                serieElem.appendChild(standingElem);
            }

            leagueElem.appendChild(serieElem);
        }

        List<PlayerStanding> leagueStandings = _leagueService.getLeagueStandings(league);
        for (PlayerStanding standing : leagueStandings) {
            Element standingElem = doc.createElement("leagueStanding");
            setStandingAttributes(standing, standingElem);
            leagueElem.appendChild(standingElem);
        }

        doc.appendChild(leagueElem);

        return doc;
    }

    public League getLeagueByType(String type) {
        for (League league : _leagueService.getActiveLeagues()) {
            if (league.getType().equals(type))
                return league;
        }
        return null;
    }

    private void setStandingAttributes(PlayerStanding standing, Element standingElem) {
        standingElem.setAttribute("player", standing.getPlayerName());
        standingElem.setAttribute("standing", String.valueOf(standing.getStanding()));
        standingElem.setAttribute("points", String.valueOf(standing.getPoints()));
        standingElem.setAttribute("gamesPlayed", String.valueOf(standing.getGamesPlayed()));
        DecimalFormat format = new DecimalFormat("##0.00%");
        standingElem.setAttribute("opponentWin", format.format(standing.getOpponentWin()));
    }
}
