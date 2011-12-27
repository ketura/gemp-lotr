package com.gempukku.lotro.server;

import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeaguePointsDAO;
import com.gempukku.lotro.db.LeagueSerieDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.sun.jersey.spi.resource.Singleton;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

@Singleton
@Path("/league")
public class LeagueResource extends AbstractResource {
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueSerieDAO _leagueSerieDao;
    @Context
    private LeaguePointsDAO _leaguePointsDao;

    @GET
    public Document getLeagueInformation() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.newDocument();
        Element leagues = doc.createElement("leagues");

        for (League league : _leagueDao.getActiveLeagues()) {
            Element leagueElem = doc.createElement("league");
            leagueElem.setAttribute("type", league.getType());
            leagueElem.setAttribute("name", league.getName());
            leagueElem.setAttribute("start", String.valueOf(league.getStart()));
            leagueElem.setAttribute("end", String.valueOf(league.getEnd()));

            List<LeagueSerie> series = _leagueSerieDao.getSeriesForLeague(league);
            for (LeagueSerie serie : series) {
                Element serieElem = doc.createElement("serie");
                serieElem.setAttribute("type", serie.getType());
                serieElem.setAttribute("maxMatches", String.valueOf(serie.getMaxMatches()));
                serieElem.setAttribute("start", String.valueOf(serie.getStart()));
                serieElem.setAttribute("end", String.valueOf(serie.getEnd()));

                final List<LeaguePointsDAO.Standing> standings = _leaguePointsDao.getSerieStandings(league, serie);
                for (LeaguePointsDAO.Standing standing : standings) {
                    Element standingElem = doc.createElement("standing");
                    standingElem.setAttribute("player", standing.getPlayer());
                    standingElem.setAttribute("points", String.valueOf(standing.getPoints()));
                    standingElem.setAttribute("gamesPlayed", String.valueOf(standing.getGamesPlayed()));
                    serieElem.appendChild(standingElem);
                }

                leagueElem.appendChild(serieElem);
            }

            List<LeaguePointsDAO.Standing> leagueStandings = _leaguePointsDao.getLeagueStandings(league);
            for (LeaguePointsDAO.Standing standing : leagueStandings) {
                Element standingElem = doc.createElement("leagueStanding");
                standingElem.setAttribute("player", standing.getPlayer());
                standingElem.setAttribute("points", String.valueOf(standing.getPoints()));
                standingElem.setAttribute("gamesPlayed", String.valueOf(standing.getGamesPlayed()));
                leagueElem.appendChild(standingElem);
            }

            leagues.appendChild(leagueElem);
        }

        doc.appendChild(leagues);

        return doc;
    }
}
