package com.gempukku.lotro.server;

import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.league.LeagueSerieData;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.league.LeagueStanding;
import com.sun.jersey.spi.resource.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.text.DecimalFormat;
import java.util.List;

@Singleton
@Path("/league")
public class LeagueResource extends AbstractResource {
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueService _leagueService;

    @GET
    public Document getLeagueInformation() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element leagues = doc.createElement("leagues");

        for (League league : _leagueService.getActiveLeagues()) {
            Element leagueElem = doc.createElement("league");
            leagueElem.setAttribute("type", league.getType());
            leagueElem.setAttribute("name", league.getName());
            leagueElem.setAttribute("start", String.valueOf(league.getStart()));
            leagueElem.setAttribute("end", String.valueOf(league.getEnd()));

            List<LeagueSerieData> series = league.getLeagueData().getSeries();
            for (LeagueSerieData serie : series) {
                Element serieElem = doc.createElement("serie");
                serieElem.setAttribute("type", serie.getName());
                serieElem.setAttribute("maxMatches", String.valueOf(serie.getMaxMatches()));
                serieElem.setAttribute("start", String.valueOf(serie.getStart()));
                serieElem.setAttribute("end", String.valueOf(serie.getEnd()));

                final List<LeagueStanding> standings = _leagueService.getLeagueSerieStandings(league, serie);
                for (LeagueStanding standing : standings) {
                    Element standingElem = doc.createElement("standing");
                    setStandingAttributes(standing, standingElem);
                    serieElem.appendChild(standingElem);
                }

                leagueElem.appendChild(serieElem);
            }

            List<LeagueStanding> leagueStandings = _leagueService.getLeagueStandings(league);
            for (LeagueStanding standing : leagueStandings) {
                Element standingElem = doc.createElement("leagueStanding");
                setStandingAttributes(standing, standingElem);
                leagueElem.appendChild(standingElem);
            }

            leagues.appendChild(leagueElem);
        }

        doc.appendChild(leagues);

        return doc;
    }

    private void setStandingAttributes(LeagueStanding standing, Element standingElem) {
        standingElem.setAttribute("player", standing.getPlayerName());
        standingElem.setAttribute("standing", String.valueOf(standing.getStanding()));
        standingElem.setAttribute("points", String.valueOf(standing.getPoints()));
        standingElem.setAttribute("gamesPlayed", String.valueOf(standing.getGamesPlayed()));
        DecimalFormat format = new DecimalFormat("##0.00%");
        standingElem.setAttribute("opponentWin", format.format(standing.getOpponentWin()));
    }
}
