package com.gempukku.lotro.league;

import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.LeaguePointsDAO;
import com.gempukku.lotro.db.LeagueSerieDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.gempukku.lotro.game.LotroGameMediator;
import com.gempukku.lotro.logic.timing.GameResultListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LeagueService {
    private Comparator<LeagueStanding> LEAGUE_STANDING_COMPARATOR =
            new MultipleComparator<LeagueStanding>(
                    new DescComparator<LeagueStanding>(new PointsComparator()),
                    new GamesPlayedComparator(),
                    new DescComparator<LeagueStanding>(new OpponentsWinComparator()));

    private LeagueDAO _leagueDao;
    private LeagueSerieDAO _leagueSeasonDao;
    private LeaguePointsDAO _leaguePointsDao;
    private LeagueMatchDAO _leagueMatchDao;

    private Map<League, List<LeagueStanding>> _leagueStandings = new ConcurrentHashMap<League, List<LeagueStanding>>();
    private Map<LeagueSerie, List<LeagueStanding>> _leagueSerieStandings = new ConcurrentHashMap<LeagueSerie, List<LeagueStanding>>();

    public LeagueService(LeagueDAO leagueDao, LeagueSerieDAO leagueSeasonDao, LeaguePointsDAO leaguePointsDao, LeagueMatchDAO leagueMatchDao) {
        _leagueDao = leagueDao;
        _leagueSeasonDao = leagueSeasonDao;
        _leaguePointsDao = leaguePointsDao;
        _leagueMatchDao = leagueMatchDao;
    }

    public Set<League> getActiveLeagues() {
        return _leagueDao.getActiveLeagues();
    }

    public League getLeagueByType(String type) {
        for (League league : getActiveLeagues()) {
            if (league.getType().equals(type))
                return league;
        }
        return null;
    }

    public LeagueSerie getCurrentLeagueSerie(League league) {
        final int startDay = getCurrentDate();

        return _leagueSeasonDao.getSerieForLeague(league, startDay);
    }

    public void leagueGameStarting(final League league, final LeagueSerie serie, LotroGameMediator gameMediator) {
        if (isRanked(league, serie, gameMediator)) {
            gameMediator.addGameResultListener(
                    new GameResultListener() {
                        @Override
                        public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                            String loser = loserPlayerIdsWithReasons.keySet().iterator().next();
                            _leagueMatchDao.addPlayedMatch(league, serie, winnerPlayerId, loser);
                            _leaguePointsDao.addPoints(league, serie, winnerPlayerId, 2);
                            _leaguePointsDao.addPoints(league, serie, loser, 1);
                            _leagueStandings.remove(league);
                            _leagueSerieStandings.remove(serie);
                        }
                    });
            gameMediator.sendMessageToPlayers("This is a ranked game in " + league.getName());
        } else {
            gameMediator.sendMessageToPlayers("This is NOT a ranked game in " + league.getName());
        }
    }

    public List<LeagueStanding> getLeagueStandings(League league) {
        List<LeagueStanding> leagueStandings = _leagueStandings.get(league);
        if (leagueStandings == null) {
            synchronized (this) {
                leagueStandings = createLeagueStandings(league);
                _leagueStandings.put(league, leagueStandings);
            }
        }
        return leagueStandings;
    }

    public List<LeagueStanding> getLeagueSerieStandings(League league, LeagueSerie leagueSerie) {
        List<LeagueStanding> serieStandings = _leagueSerieStandings.get(leagueSerie);
        if (serieStandings == null) {
            synchronized (this) {
                serieStandings = createLeagueSerieStandings(league, leagueSerie);
                _leagueSerieStandings.put(leagueSerie, serieStandings);
            }
        }
        return serieStandings;
    }

    private List<LeagueStanding> createLeagueSerieStandings(League league, LeagueSerie leagueSerie) {
        final Map<String, LeaguePointsDAO.Points> points = _leaguePointsDao.getLeagueSeriePoints(league, leagueSerie);
        final Collection<LeagueMatch> matches = _leagueMatchDao.getLeagueSerieMatches(league, leagueSerie);

        return createStandingsForMatchesAndPoints(points, matches);
    }

    private List<LeagueStanding> createLeagueStandings(League league) {
        final Map<String, LeaguePointsDAO.Points> points = _leaguePointsDao.getLeaguePoints(league);
        final Collection<LeagueMatch> matches = _leagueMatchDao.getLeagueMatches(league);

        return createStandingsForMatchesAndPoints(points, matches);
    }

    private List<LeagueStanding> createStandingsForMatchesAndPoints(Map<String, LeaguePointsDAO.Points> points, Collection<LeagueMatch> matches) {
        Map<String, List<String>> playerOpponents = new HashMap<String, List<String>>();
        for (LeagueMatch leagueMatch : matches) {
            appendPlayer(playerOpponents, leagueMatch.getWinner(), leagueMatch.getLoser());
            appendPlayer(playerOpponents, leagueMatch.getLoser(), leagueMatch.getWinner());
        }

        List<LeagueStanding> leagueStandings = new LinkedList<LeagueStanding>();
        for (Map.Entry<String, LeaguePointsDAO.Points> playerPoints : points.entrySet()) {
            LeagueStanding standing = new LeagueStanding(playerPoints.getKey(), playerPoints.getValue().getPoints(), playerPoints.getValue().getGamesPlayed());
            List<String> opponents = playerOpponents.get(playerPoints.getKey());
            int opponentWins = 0;
            int opponentGames = 0;
            for (String opponent : opponents) {
                final LeaguePointsDAO.Points opponentPoints = points.get(opponent);
                opponentWins += opponentPoints.getPoints() - opponentPoints.getGamesPlayed();
                opponentGames += opponentPoints.getGamesPlayed();
            }
            standing.setOpponentWin(opponentWins * 1f / opponentGames);
            leagueStandings.add(standing);
        }

        Collections.sort(leagueStandings, LEAGUE_STANDING_COMPARATOR);

        int standing = 0;
        int position = 1;
        LeagueStanding lastStanding = null;
        for (LeagueStanding leagueStanding : leagueStandings) {
            if (lastStanding == null || LEAGUE_STANDING_COMPARATOR.compare(leagueStanding, lastStanding) != 0)
                standing = position;
            leagueStanding.setStanding(standing);
            position++;
            lastStanding = leagueStanding;
        }
        return leagueStandings;
    }

    private void appendPlayer(Map<String, List<String>> playerOpponents, String player, String opponent) {
        List<String> opponents = playerOpponents.get(player);
        if (opponents == null) {
            opponents = new LinkedList<String>();
            playerOpponents.put(player, opponents);
        }
        opponents.add(opponent);
    }

    public boolean canPlayRankedGame(League league, LeagueSerie season, String player) {
        int maxMatches = season.getMaxMatches();
        Collection<LeagueMatch> playedInSeason = _leagueMatchDao.getPlayerMatchesPlayedOn(league, season, player);
        if (playedInSeason.size() >= maxMatches)
            return false;
        return true;
    }

    public boolean canPlayRankedGame(League league, LeagueSerie season, String playerOne, String playerTwo) {
        Collection<LeagueMatch> playedInSeason = _leagueMatchDao.getPlayerMatchesPlayedOn(league, season, playerOne);
        for (LeagueMatch leagueMatch : playedInSeason) {
            if (playerTwo.equals(leagueMatch.getWinner()) && playerTwo.equals(leagueMatch.getLoser()))
                return false;
        }
        return true;
    }

    private int getCurrentDate() {
        Calendar date = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        return date.get(Calendar.YEAR) * 10000 + (date.get(Calendar.MONTH) + 1) * 100 + date.get(Calendar.DAY_OF_MONTH);
    }

    private boolean isRanked(League league, LeagueSerie season, LotroGameMediator gameMediator) {
        Set<String> playersPlaying = gameMediator.getPlayersPlaying();
        for (String player : playersPlaying) {
            int maxMatches = season.getMaxMatches();
            Collection<LeagueMatch> playedInSeason = _leagueMatchDao.getPlayerMatchesPlayedOn(league, season, player);
            if (playedInSeason.size() >= maxMatches)
                return false;
            for (LeagueMatch leagueMatch : playedInSeason) {
                if (playersPlaying.contains(leagueMatch.getWinner()) && playersPlaying.contains(leagueMatch.getLoser()))
                    return false;
            }
        }
        return true;
    }

    private class MultipleComparator<T> implements Comparator<T> {
        private Comparator<T>[] _comparators;

        public MultipleComparator(Comparator<T>... comparators) {
            _comparators = comparators;
        }

        @Override
        public int compare(T o1, T o2) {
            for (Comparator<T> comparator : _comparators) {
                int result = comparator.compare(o1, o2);
                if (result != 0)
                    return result;
            }
            return 0;
        }
    }

    private class DescComparator<T> implements Comparator<T> {
        private Comparator<T> _comparator;

        private DescComparator(Comparator<T> comparator) {
            _comparator = comparator;
        }

        @Override
        public int compare(T o1, T o2) {
            return _comparator.compare(o2, o1);
        }
    }

    private class PointsComparator implements Comparator<LeagueStanding> {
        @Override
        public int compare(LeagueStanding o1, LeagueStanding o2) {
            return o1.getPoints() - o2.getPoints();
        }
    }

    private class GamesPlayedComparator implements Comparator<LeagueStanding> {
        @Override
        public int compare(LeagueStanding o1, LeagueStanding o2) {
            return o1.getGamesPlayed() - o2.getGamesPlayed();
        }
    }

    private class OpponentsWinComparator implements Comparator<LeagueStanding> {
        @Override
        public int compare(LeagueStanding o1, LeagueStanding o2) {
            final float diff = o1.getOpponentWin() - o2.getOpponentWin();
            if (diff < 0)
                return -1;
            if (diff > 0)
                return 1;
            return 0;
        }
    }
}
