package com.gempukku.lotro.league;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.LeagueParticipationDAO;
import com.gempukku.lotro.db.LeaguePointsDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LeagueServiceTest {
    @Test
    public void testJoiningLeagueAfterMaxGamesPlayed() throws Exception {
        LeagueDAO leagueDao = Mockito.mock(LeagueDAO.class);

        StringBuilder sb = new StringBuilder();
        sb.append("20120502" + "," + "default" + "," + "1" + "," + "1");
        for (int i = 0; i < 1; i++)
            sb.append("," + "lotr_block" + "," + "7" + "," + "2");


        List<League> leagues = new ArrayList<League>();
        League league = new League(5000, "League name", "leagueType", NewConstructedLeagueData.class.getName(), sb.toString(), 0);
        leagues.add(league);

        LeagueSerieData leagueSerie = league.getLeagueData().getSeries().get(0);

        Mockito.when(leagueDao.loadActiveLeagues(Mockito.anyInt())).thenReturn(leagues);

        LeaguePointsDAO leaguePointsDAO = Mockito.mock(LeaguePointsDAO.class);
        LeagueMatchDAO leagueMatchDAO = Mockito.mock(LeagueMatchDAO.class);

        Set<LeagueMatch> matches = new HashSet<LeagueMatch>();

        Mockito.when(leagueMatchDAO.getLeagueMatches(league)).thenReturn(new HashSet<LeagueMatch>(matches));
        Mockito.when(leagueMatchDAO.getLeagueSerieMatches(league, leagueSerie)).thenReturn(new HashSet<LeagueMatch>(matches));

        LeagueParticipationDAO leagueParticipationDAO = Mockito.mock(LeagueParticipationDAO.class);
        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);

        LeagueService leagueService = new LeagueService(leagueDao, leaguePointsDAO, leagueMatchDAO, leagueParticipationDAO, collectionsManager);

        assertTrue(leagueService.canPlayRankedGame(league, leagueSerie, "player1"));
        assertTrue(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player2"));

        leagueService.reportLeagueGameResult(league, leagueSerie, "player1", "player2");

        assertTrue(leagueService.canPlayRankedGame(league, leagueSerie, "player1"));
        assertFalse(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player2"));
        assertTrue(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player3"));

        Mockito.verify(leagueMatchDAO).getLeagueMatches(league);
        Mockito.verify(leagueMatchDAO).getLeagueSerieMatches(league, leagueSerie);

        Mockito.verify(leagueMatchDAO).addPlayedMatch(league, leagueSerie, "player1", "player2");
        Mockito.verifyNoMoreInteractions(leagueMatchDAO);

        leagueService.reportLeagueGameResult(league, leagueSerie, "player1", "player3");

        assertFalse(leagueService.canPlayRankedGame(league, leagueSerie, "player1"));
        assertFalse(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player2"));
        assertFalse(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player3"));

        Mockito.verify(leagueMatchDAO).addPlayedMatch(league, leagueSerie, "player1", "player3");
        Mockito.verifyNoMoreInteractions(leagueMatchDAO);
    }

    @Test
    public void testJoiningLeagueAfterMaxGamesPlayedWithPreloadedDb() throws Exception {
        LeagueDAO leagueDao = Mockito.mock(LeagueDAO.class);

        StringBuilder sb = new StringBuilder();
        sb.append("20120502" + "," + "default" + "," + "1" + "," + "1");
        for (int i = 0; i < 1; i++)
            sb.append("," + "lotr_block" + "," + "7" + "," + "2");


        List<League> leagues = new ArrayList<League>();
        League league = new League(5000, "League name", "leagueType", NewConstructedLeagueData.class.getName(), sb.toString(), 0);
        leagues.add(league);

        LeagueSerieData leagueSerie = league.getLeagueData().getSeries().get(0);

        Mockito.when(leagueDao.loadActiveLeagues(Mockito.anyInt())).thenReturn(leagues);

        LeaguePointsDAO leaguePointsDAO = Mockito.mock(LeaguePointsDAO.class);
        LeagueMatchDAO leagueMatchDAO = Mockito.mock(LeagueMatchDAO.class);

        Set<LeagueMatch> matches = new HashSet<LeagueMatch>();
        matches.add(new LeagueMatch("player1", "player2"));

        Mockito.when(leagueMatchDAO.getLeagueMatches(league)).thenReturn(new HashSet<LeagueMatch>(matches));
        Mockito.when(leagueMatchDAO.getLeagueSerieMatches(league, leagueSerie)).thenReturn(new HashSet<LeagueMatch>(matches));

        LeagueParticipationDAO leagueParticipationDAO = Mockito.mock(LeagueParticipationDAO.class);
        CollectionsManager collectionsManager = Mockito.mock(CollectionsManager.class);

        LeagueService leagueService = new LeagueService(leagueDao, leaguePointsDAO, leagueMatchDAO, leagueParticipationDAO, collectionsManager);

        assertTrue(leagueService.canPlayRankedGame(league, leagueSerie, "player1"));
        assertFalse(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player2"));
        assertTrue(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player3"));

        leagueService.reportLeagueGameResult(league, leagueSerie, "player1", "player3");

        Mockito.verify(leagueMatchDAO).getLeagueMatches(league);
        Mockito.verify(leagueMatchDAO).getLeagueSerieMatches(league, leagueSerie);

        Mockito.verify(leagueMatchDAO).addPlayedMatch(league, leagueSerie, "player1", "player3");
        Mockito.verifyNoMoreInteractions(leagueMatchDAO);

        assertFalse(leagueService.canPlayRankedGame(league, leagueSerie, "player1"));
        assertFalse(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player2"));
        assertFalse(leagueService.canPlayRankedGameAgainst(league, leagueSerie, "player1", "player3"));
    }
}
