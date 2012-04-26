package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.cache.ExpireObjectCache;
import com.gempukku.lotro.cache.Producable;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.LeagueParticipationDAO;
import com.gempukku.lotro.db.LeaguePointsDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;
import com.gempukku.util.DescComparator;
import com.gempukku.util.MultipleComparator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class LeagueService {
    private Comparator<LeagueStanding> LEAGUE_STANDING_COMPARATOR =
            new MultipleComparator<LeagueStanding>(
                    new DescComparator<LeagueStanding>(new PointsComparator()),
                    new GamesPlayedComparator(),
                    new DescComparator<LeagueStanding>(new OpponentsWinComparator()));

    private LeagueDAO _leagueDao;
    private LeaguePointsDAO _leaguePointsDao;
    // Cached on this layer
    private LeagueMatchDAO _leagueMatchDao;
    private LeagueParticipationDAO _leagueParticipationDAO;
    private CollectionsManager _collectionsManager;

    private Map<League, List<LeagueStanding>> _leagueStandings = new ConcurrentHashMap<League, List<LeagueStanding>>();
    private Map<LeagueSerieData, List<LeagueStanding>> _leagueSerieStandings = new ConcurrentHashMap<LeagueSerieData, List<LeagueStanding>>();

    private Map<League, Set<String>> _playersParticipating = new ConcurrentHashMap<League, Set<String>>();
    private Map<League, Set<String>> _playersNotParticipating = new ConcurrentHashMap<League, Set<String>>();

    private ExpireObjectCache<League, Collection<LeagueMatch>> _cachedLeagueMatches = new ExpireObjectCache<League, Collection<LeagueMatch>>();
    private ExpireObjectCache<LeagueSerieData, Collection<LeagueMatch>> _cachedSerieMatches = new ExpireObjectCache<LeagueSerieData, Collection<LeagueMatch>>();

    private ExpireObjectCache<League, Map<String, LeaguePointsDAO.Points>> _cachedLeaguePoints = new ExpireObjectCache<League, Map<String, LeaguePointsDAO.Points>>();
    private ExpireObjectCache<LeagueSerieData, Map<String, LeaguePointsDAO.Points>> _cachedSeriePoints = new ExpireObjectCache<LeagueSerieData, Map<String, LeaguePointsDAO.Points>>();

    private int _activeLeaguesLoadedDate;
    private List<League> _activeLeagues;

    public LeagueService(LeagueDAO leagueDao, LeaguePointsDAO leaguePointsDao, LeagueMatchDAO leagueMatchDao,
                         LeagueParticipationDAO leagueParticipationDAO, CollectionsManager collectionsManager) {
        _leagueDao = leagueDao;
        _leaguePointsDao = leaguePointsDao;
        _leagueMatchDao = leagueMatchDao;
        _leagueParticipationDAO = leagueParticipationDAO;
        _collectionsManager = collectionsManager;
    }

    public void clearCache() {
        _leagueSerieStandings.clear();
        _leagueStandings.clear();
        _activeLeaguesLoadedDate = 0;

        _cachedLeagueMatches.clearCache();
        _cachedLeaguePoints.clearCache();
        _cachedSerieMatches.clearCache();
        _cachedSeriePoints.clearCache();
    }

    private synchronized void ensureLoadedCurrentLeagues() {
        int currentDate = DateUtils.getCurrentDate();
        if (currentDate != _activeLeaguesLoadedDate) {
            _cachedLeagueMatches.clearCache();
            _cachedLeaguePoints.clearCache();
            _cachedSerieMatches.clearCache();
            _cachedSeriePoints.clearCache();

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

    private Collection<LeagueMatch> getLeagueMatches(final League league) {
        return _cachedLeagueMatches.getCachedObject(league,
                new Producable<League, Collection<LeagueMatch>>() {
                    @Override
                    public Collection<LeagueMatch> produce(League key) {
                        return new CopyOnWriteArraySet<LeagueMatch>(_leagueMatchDao.getLeagueMatches(league));
                    }
                });
    }

    private Collection<LeagueMatch> getLeagueSerieMatches(final League league, final LeagueSerieData serie) {
        return _cachedSerieMatches.getCachedObject(serie,
                new Producable<LeagueSerieData, Collection<LeagueMatch>>() {
                    @Override
                    public Collection<LeagueMatch> produce(LeagueSerieData key) {
                        return new CopyOnWriteArraySet<LeagueMatch>(_leagueMatchDao.getLeagueSerieMatches(league, serie));
                    }
                });
    }

    private Map<String, LeaguePointsDAO.Points> getLeaguePoints(final League league) {
        return _cachedLeaguePoints.getCachedObject(league,
                new Producable<League, Map<String, LeaguePointsDAO.Points>>() {
                    @Override
                    public Map<String, LeaguePointsDAO.Points> produce(League key) {
                        return new ConcurrentHashMap<String, LeaguePointsDAO.Points>(_leaguePointsDao.getLeaguePoints(league));
                    }
                });
    }

    private Map<String, LeaguePointsDAO.Points> getLeagueSeriePoints(final League league, final LeagueSerieData serie) {
        return _cachedSeriePoints.getCachedObject(serie,
                new Producable<LeagueSerieData, Map<String, LeaguePointsDAO.Points>>() {
                    @Override
                    public Map<String, LeaguePointsDAO.Points> produce(LeagueSerieData key) {
                        return new ConcurrentHashMap<String, LeaguePointsDAO.Points>(_leaguePointsDao.getLeagueSeriePoints(league, serie));
                    }
                });
    }

    public List<League> getActiveLeagues() {
        if (DateUtils.getCurrentDate() == _activeLeaguesLoadedDate)
            return Collections.unmodifiableList(_activeLeagues);
        else {
            ensureLoadedCurrentLeagues();
            return Collections.unmodifiableList(_activeLeagues);
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

    public synchronized boolean isPlayerInLeague(League league, Player player) {
        Set<String> playersParticipating = _playersParticipating.get(league);
        if (playersParticipating != null && playersParticipating.contains(player.getName()))
            return true;

        Set<String> playersNotParticipating = _playersNotParticipating.get(league);
        if (playersNotParticipating != null && playersNotParticipating.contains(player.getName()))
            return false;

        try {
            final boolean userParticipating = _leagueParticipationDAO.isUserParticipating(league, player);
            if (userParticipating) {
                if (playersParticipating == null) {
                    playersParticipating = new HashSet<String>();
                    _playersParticipating.put(league, playersParticipating);
                }
                playersParticipating.add(player.getName());
            } else {
                if (playersNotParticipating == null) {
                    playersNotParticipating = new HashSet<String>();
                    _playersNotParticipating.put(league, playersNotParticipating);
                }
                playersNotParticipating.add(player.getName());
            }

            return userParticipating;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to retrieve information if user is part of the league");
        }
    }

    public synchronized boolean playerJoinsLeague(League league, Player player) {
        if (isPlayerInLeague(league, player))
            return false;
        int cost = league.getCost();
        if (_collectionsManager.removeCurrencyFromPlayerCollection(player, new CollectionType("permanent", "My cards"), cost)) {
            try {
                _leagueParticipationDAO.userJoinsLeague(league, player);
                Set<String> notParticipating = _playersNotParticipating.get(league);
                if (notParticipating != null)
                    notParticipating.remove(player.getName());
                Set<String> participating = _playersParticipating.get(league);
                if (participating != null)
                    participating.add(player.getName());
                league.getLeagueData().joinLeague(_collectionsManager, player, DateUtils.getCurrentDate());
                return true;
            } catch (SQLException exp) {
                throw new RuntimeException("Unable to add user to league");
            }
        } else {
            return false;
        }
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
        addMatch(league, serie, winner, loser);

        addPoints(league, serie, winner, 2);
        addPoints(league, serie, loser, 1);

        _leagueStandings.remove(league);
        _leagueSerieStandings.remove(serie);

        awardPrizesToPlayer(league, serie, winner, true);
        awardPrizesToPlayer(league, serie, loser, false);
    }

    private void addPoints(League league, LeagueSerieData serie, String player, int points) {
        Map<String, LeaguePointsDAO.Points> leaguePoints = getLeaguePoints(league);
        Map<String, LeaguePointsDAO.Points> leagueSeriePoints = getLeagueSeriePoints(league, serie);
        _leaguePointsDao.addPoints(league, serie, player, points);
        storeNewLeaguePoints(leaguePoints, player, points);
        storeNewLeagueSeriePoints(leagueSeriePoints, player, points);
    }

    private void storeNewLeagueSeriePoints(Map<String, LeaguePointsDAO.Points> leagueSeriePoints, String player, int points) {
        final LeaguePointsDAO.Points playerPoints = leagueSeriePoints.get(player);
        if (playerPoints != null)
            playerPoints.addPointsForMatch(points);
        else {
            LeaguePointsDAO.Points newPoints = new LeaguePointsDAO.Points(points, 1);
            leagueSeriePoints.put(player, newPoints);
        }
    }

    private void storeNewLeaguePoints(Map<String, LeaguePointsDAO.Points> leaguePoints, String player, int points) {
        final LeaguePointsDAO.Points playerPoints = leaguePoints.get(player);
        if (playerPoints != null)
            playerPoints.addPointsForMatch(points);
        else {
            LeaguePointsDAO.Points newPoints = new LeaguePointsDAO.Points(points, 1);
            leaguePoints.put(player, newPoints);
        }
    }

    private void addMatch(League league, LeagueSerieData serie, String winner, String loser) {
        Collection<LeagueMatch> leagueMatches = getLeagueMatches(league);
        Collection<LeagueMatch> leagueSerieMatches = getLeagueSerieMatches(league, serie);
        LeagueMatch match = new LeagueMatch(winner, loser);
        _leagueMatchDao.addPlayedMatch(league, serie, match);
        leagueMatches.add(match);
        leagueSerieMatches.add(match);
    }

    private void awardPrizesToPlayer(League league, LeagueSerieData serie, String player, boolean winner) {
        int count = 0;
        Collection<LeagueMatch> playerMatchesPlayedOn = getPlayerMatchesInSerie(league, serie, player);
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

    private Collection<LeagueMatch> getPlayerMatchesInSerie(League league, LeagueSerieData serie, String player) {
        final Collection<LeagueMatch> allMatches = getLeagueSerieMatches(league, serie);
        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
        for (LeagueMatch match : allMatches) {
            if (match.getWinner().equals(player) || match.getLoser().equals(player))
                result.add(match);
        }
        return result;
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
        final Map<String, LeaguePointsDAO.Points> points = getLeagueSeriePoints(league, leagueSerie);
        final Collection<LeagueMatch> matches = getLeagueSerieMatches(league, leagueSerie);

        return createStandingsForMatchesAndPoints(points, matches);
    }

    private List<LeagueStanding> createLeagueStandings(League league) {
        final Map<String, LeaguePointsDAO.Points> points = getLeaguePoints(league);
        final Collection<LeagueMatch> matches = getLeagueMatches(league);

        return createStandingsForMatchesAndPoints(points, matches);
    }

    private List<LeagueStanding> createStandingsForMatchesAndPoints(Map<String, LeaguePointsDAO.Points> points, Collection<LeagueMatch> matches) {
        Map<String, List<String>> playerOpponents = new HashMap<String, List<String>>();
        Map<String, Integer> playerWins = new HashMap<String, Integer>();
        Map<String, Integer> playerLoss = new HashMap<String, Integer>();
        for (LeagueMatch leagueMatch : matches) {
            appendPlayer(playerOpponents, leagueMatch.getWinner(), leagueMatch.getLoser());
            appendPlayer(playerOpponents, leagueMatch.getLoser(), leagueMatch.getWinner());
            appendMatch(playerWins, playerLoss, leagueMatch.getWinner(), leagueMatch.getLoser());
        }

        List<LeagueStanding> leagueStandings = new LinkedList<LeagueStanding>();
        for (Map.Entry<String, LeaguePointsDAO.Points> playerPoints : points.entrySet()) {
            LeagueStanding standing = new LeagueStanding(playerPoints.getKey(), playerPoints.getValue().getPoints(), playerPoints.getValue().getGamesPlayed());
            List<String> opponents = playerOpponents.get(playerPoints.getKey());
            int opponentWins = 0;
            int opponentGames = 0;
            for (String opponent : opponents) {
                opponentWins += playerWins.get(opponent);
                opponentGames += playerWins.get(opponent) + playerLoss.get(opponent);
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

    private void appendMatch(Map<String, Integer> playerWins, Map<String, Integer> playerLoss, String winner, String loser) {
        append(playerWins, winner);
        append(playerLoss, loser);
        if (!playerWins.containsKey(loser))
            playerWins.put(loser, 0);
        if (!playerLoss.containsKey(winner))
            playerLoss.put(winner, 0);
    }

    private void append(Map<String, Integer> playerCounts, String player) {
        Integer count = playerCounts.get(player);
        if (count == null)
            count = 0;
        count++;
        playerCounts.put(player, count);
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
        Collection<LeagueMatch> playedInSeason = getPlayerMatchesInSerie(league, season, player);
        if (playedInSeason.size() >= maxMatches)
            return false;
        return true;
    }

    public boolean canPlayRankedGame(League league, LeagueSerieData season, String playerOne, String playerTwo) {
        Collection<LeagueMatch> playedInSeason = getPlayerMatchesInSerie(league, season, playerOne);
        for (LeagueMatch leagueMatch : playedInSeason) {
            if (playerTwo.equals(leagueMatch.getWinner()) || playerTwo.equals(leagueMatch.getLoser()))
                return false;
        }
        return true;
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
