package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.LeaguePointsDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LeagueService {
    private Comparator<LeagueStanding> LEAGUE_STANDING_COMPARATOR =
            new MultipleComparator<LeagueStanding>(
                    new DescComparator<LeagueStanding>(new PointsComparator()),
                    new GamesPlayedComparator(),
                    new DescComparator<LeagueStanding>(new OpponentsWinComparator()));

    private LeagueDAO _leagueDao;
    private LeaguePointsDAO _leaguePointsDao;
    private LeagueMatchDAO _leagueMatchDao;
    private CollectionsManager _collectionsManager;

    private Map<League, List<LeagueStanding>> _leagueStandings = new ConcurrentHashMap<League, List<LeagueStanding>>();
    private Map<LeagueSerieData, List<LeagueStanding>> _leagueSerieStandings = new ConcurrentHashMap<LeagueSerieData, List<LeagueStanding>>();

    private int _activeLeaguesLoadedDate;
    private Set<League> _activeLeagues;

    public LeagueService(LeagueDAO leagueDao, LeaguePointsDAO leaguePointsDao, LeagueMatchDAO leagueMatchDao,
                         CollectionsManager collectionsManager) {
        _leagueDao = leagueDao;
        _leaguePointsDao = leaguePointsDao;
        _leagueMatchDao = leagueMatchDao;
        _collectionsManager = collectionsManager;
    }

    public void clearCache() {
        _leagueSerieStandings.clear();
        _leagueStandings.clear();
        _activeLeaguesLoadedDate = 0;
    }

    private synchronized void ensureLoadedCurrentLeagues() {
        int currentDate = DateUtils.getCurrentDate();
        if (currentDate != _activeLeaguesLoadedDate) {
            try {
                _activeLeagues = _leagueDao.loadActiveLeagues(currentDate);
                _activeLeaguesLoadedDate = currentDate;
                processLoadedLeagues(currentDate);
            } catch (SQLException e) {
                throw new RuntimeException("Unable to load Leagues", e);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load Leagues", e);
            }
        }
    }

    public Set<League> getActiveLeagues() {
        if (DateUtils.getCurrentDate() == _activeLeaguesLoadedDate)
            return Collections.unmodifiableSet(_activeLeagues);
        else {
            ensureLoadedCurrentLeagues();
            return Collections.unmodifiableSet(_activeLeagues);
        }
    }

    private void processLoadedLeagues(int currentDate) {
        for (League activeLeague : _activeLeagues) {
            int oldStatus = activeLeague.getStatus();
            int newStatus = activeLeague.getLeagueData().process(_collectionsManager, getLeagueStandings(activeLeague), oldStatus, currentDate);
            if (newStatus != oldStatus)
                _leagueDao.setStatus(activeLeague, newStatus);
        }
    }

    public synchronized CardCollection ensurePlayerHasCollection(Player player, String collectionType) {
        for (League league : getActiveLeagues()) {
            LeagueData leagueData = league.getLeagueData();
            for (LeagueSerieData leagueSerieData : leagueData.getSeries()) {
                CollectionType serieCollectionType = leagueSerieData.getCollectionType();
                if (serieCollectionType != null && serieCollectionType.getCode().equals(collectionType)) {
                    return leagueData.joinLeague(_collectionsManager, player, DateUtils.getCurrentDate());
                }
            }
        }
        return null;
    }

    public League getLeagueByType(String type) {
        for (League league : getActiveLeagues()) {
            if (league.getType().equals(type))
                return league;
        }
        return null;
    }

    public CollectionType getCollectionTypeByCode(String collectionTypeCode) {
        for (League league : getActiveLeagues()) {
            for (LeagueSerieData leagueSerieData : league.getLeagueData().getSeries()) {
                CollectionType collectionType = leagueSerieData.getCollectionType();
                if (collectionType != null && collectionType.getCode().equals(collectionTypeCode))
                    return collectionType;
            }
        }
        return null;
    }

    public LeagueSerieData getCurrentLeagueSerie(League league) {
        final int currentDate = DateUtils.getCurrentDate();

        for (LeagueSerieData leagueSerieData : league.getLeagueData().getSeries()) {
            if (currentDate >= leagueSerieData.getStart() && currentDate <= leagueSerieData.getEnd())
                return leagueSerieData;
        }

        return null;
    }

    public void reportLeagueGameResult(League league, LeagueSerieData serie, String winner, String loser) {
        _leagueMatchDao.addPlayedMatch(league, serie, winner, loser);
        _leaguePointsDao.addPoints(league, serie, winner, 2);
        _leaguePointsDao.addPoints(league, serie, loser, 1);
        _leagueStandings.remove(league);
        _leagueSerieStandings.remove(serie);

        awardPrizesToPlayer(league, serie, winner, true);
        awardPrizesToPlayer(league, serie, loser, false);
    }

    private void awardPrizesToPlayer(League league, LeagueSerieData serie, String player, boolean winner) {
        int count = 0;
        Collection<LeagueMatch> playerMatchesPlayedOn = _leagueMatchDao.getPlayerMatchesPlayedOn(league, serie, player);
        for (LeagueMatch leagueMatch : playerMatchesPlayedOn) {
            if (leagueMatch.getWinner().equals(player))
                count++;
        }

        CardCollection prize;
        if (winner)
            prize = serie.getPrizeForLeagueMatchWinner(count, playerMatchesPlayedOn.size());
        else
            prize = serie.getPrizeForLeagueMatchLoser(count, playerMatchesPlayedOn.size());
        if (prize != null)
            _collectionsManager.addItemsToPlayerCollection(player, new CollectionType("permanent", "My cards"), prize.getAll());
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

    public List<LeagueStanding> getLeagueSerieStandings(League league, LeagueSerieData leagueSerie) {
        List<LeagueStanding> serieStandings = _leagueSerieStandings.get(leagueSerie);
        if (serieStandings == null) {
            synchronized (this) {
                serieStandings = createLeagueSerieStandings(league, leagueSerie);
                _leagueSerieStandings.put(leagueSerie, serieStandings);
            }
        }
        return serieStandings;
    }

    private List<LeagueStanding> createLeagueSerieStandings(League league, LeagueSerieData leagueSerie) {
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

    public boolean canPlayRankedGame(League league, LeagueSerieData season, String player) {
        int maxMatches = season.getMaxMatches();
        Collection<LeagueMatch> playedInSeason = _leagueMatchDao.getPlayerMatchesPlayedOn(league, season, player);
        if (playedInSeason.size() >= maxMatches)
            return false;
        return true;
    }

    public boolean canPlayRankedGame(League league, LeagueSerieData season, String playerOne, String playerTwo) {
        Collection<LeagueMatch> playedInSeason = _leagueMatchDao.getPlayerMatchesPlayedOn(league, season, playerOne);
        for (LeagueMatch leagueMatch : playedInSeason) {
            if (playerTwo.equals(leagueMatch.getWinner()) || playerTwo.equals(leagueMatch.getLoser()))
                return false;
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
