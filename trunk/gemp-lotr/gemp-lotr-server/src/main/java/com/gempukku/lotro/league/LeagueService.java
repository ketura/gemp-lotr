package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.PlayerStanding;
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

public class LeagueService {
    private Comparator<PlayerStanding> LEAGUE_STANDING_COMPARATOR =
            new MultipleComparator<PlayerStanding>(
                    new DescComparator<PlayerStanding>(new PointsComparator()),
                    new GamesPlayedComparator(),
                    new DescComparator<PlayerStanding>(new OpponentsWinComparator()));

    private LeagueDAO _leagueDao;

    // Cached on this layer
    private CachedLeaguePointsDAO _leaguePointsDao;
    private CachedLeagueMatchDAO _leagueMatchDao;
    private CachedLeagueParticipationDAO _leagueParticipationDAO;

    private CollectionsManager _collectionsManager;

    private Map<String, List<PlayerStanding>> _leagueStandings = new ConcurrentHashMap<String, List<PlayerStanding>>();
    private Map<String, List<PlayerStanding>> _leagueSerieStandings = new ConcurrentHashMap<String, List<PlayerStanding>>();

    private int _activeLeaguesLoadedDate;
    private List<League> _activeLeagues;

    public LeagueService(LeagueDAO leagueDao, LeaguePointsDAO leaguePointsDao, LeagueMatchDAO leagueMatchDao,
                         LeagueParticipationDAO leagueParticipationDAO, CollectionsManager collectionsManager) {
        _leagueDao = leagueDao;
        _leaguePointsDao = new CachedLeaguePointsDAO(leaguePointsDao);
        _leagueMatchDao = new CachedLeagueMatchDAO(leagueMatchDao);
        _leagueParticipationDAO = new CachedLeagueParticipationDAO(leagueParticipationDAO);
        _collectionsManager = collectionsManager;
    }

    public void clearCache() {
        _leagueSerieStandings.clear();
        _leagueStandings.clear();
        _activeLeaguesLoadedDate = 0;

        _leagueMatchDao.clearCache();
        _leaguePointsDao.clearCache();
        _leagueParticipationDAO.clearCache();
    }

    private synchronized void ensureLoadedCurrentLeagues() {
        int currentDate = DateUtils.getCurrentDate();
        if (currentDate != _activeLeaguesLoadedDate) {
            _leagueMatchDao.clearCache();
            _leaguePointsDao.clearCache();
            _leagueParticipationDAO.clearCache();

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

    public boolean isPlayerInLeague(League league, Player player) {
        return _leagueParticipationDAO.getUsersParticipating(league).contains(player.getName());
    }

    public boolean playerJoinsLeague(League league, Player player) {
        if (isPlayerInLeague(league, player))
            return false;
        int cost = league.getCost();
        if (_collectionsManager.removeCurrencyFromPlayerCollection(player, new CollectionType("permanent", "My cards"), cost)) {
            _leagueParticipationDAO.userJoinsLeague(league, player);
            league.getLeagueData().joinLeague(_collectionsManager, player, DateUtils.getCurrentDate());
            return true;
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
        _leagueMatchDao.addPlayedMatch(league, serie, winner, loser);

        _leaguePointsDao.addPoints(league, serie, winner, 2);
        _leaguePointsDao.addPoints(league, serie, loser, 1);

        _leagueStandings.remove(LeagueMapKeys.getLeagueMapKey(league));
        _leagueSerieStandings.remove(LeagueMapKeys.getLeagueSerieMapKey(league, serie));

        awardPrizesToPlayer(league, serie, winner, true);
        awardPrizesToPlayer(league, serie, loser, false);
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
        final Collection<LeagueMatch> allMatches = _leagueMatchDao.getLeagueMatches(league);
        Set<LeagueMatch> result = new HashSet<LeagueMatch>();
        for (LeagueMatch match : allMatches) {
            if (match.getSerieName().equals(serie.getName()) && (match.getWinner().equals(player) || match.getLoser().equals(player)))
                result.add(match);
        }
        return result;
    }

    public List<PlayerStanding> getLeagueStandings(League league) {
        List<PlayerStanding> leagueStandings = _leagueStandings.get(LeagueMapKeys.getLeagueMapKey(league));
        if (leagueStandings == null) {
            synchronized (this) {
                leagueStandings = createLeagueStandings(league);
                _leagueStandings.put(LeagueMapKeys.getLeagueMapKey(league), leagueStandings);
            }
        }
        return leagueStandings;
    }

    public List<PlayerStanding> getLeagueSerieStandings(League league, LeagueSerieData leagueSerie) {
        List<PlayerStanding> serieStandings = _leagueSerieStandings.get(LeagueMapKeys.getLeagueSerieMapKey(league, leagueSerie));
        if (serieStandings == null) {
            synchronized (this) {
                serieStandings = createLeagueSerieStandings(league, leagueSerie);
                _leagueSerieStandings.put(LeagueMapKeys.getLeagueSerieMapKey(league, leagueSerie), serieStandings);
            }
        }
        return serieStandings;
    }

    private List<PlayerStanding> createLeagueSerieStandings(League league, LeagueSerieData leagueSerie) {
        final Map<String, LeaguePointsDAO.Points> points = _leaguePointsDao.getLeagueSeriePoints(league, leagueSerie);
        final Collection<LeagueMatch> matches = _leagueMatchDao.getLeagueMatches(league);

        Set<LeagueMatch> matchesInSerie = new HashSet<LeagueMatch>();
        for (LeagueMatch match : matches) {
            if (match.getSerieName().equals(leagueSerie.getName()))
                matchesInSerie.add(match);
        }

        return createStandingsForMatchesAndPoints(points, matchesInSerie);
    }

    private List<PlayerStanding> createLeagueStandings(League league) {
        final Map<String, LeaguePointsDAO.Points> points = _leaguePointsDao.getLeaguePoints(league);
        final Collection<LeagueMatch> matches = _leagueMatchDao.getLeagueMatches(league);

        return createStandingsForMatchesAndPoints(points, matches);
    }

    private List<PlayerStanding> createStandingsForMatchesAndPoints(Map<String, LeaguePointsDAO.Points> points, Collection<LeagueMatch> matches) {
        Map<String, List<String>> playerOpponents = new HashMap<String, List<String>>();
        Map<String, Integer> playerWins = new HashMap<String, Integer>();
        Map<String, Integer> playerLoss = new HashMap<String, Integer>();
        for (LeagueMatch leagueMatch : matches) {
            appendPlayer(playerOpponents, leagueMatch.getWinner(), leagueMatch.getLoser());
            appendPlayer(playerOpponents, leagueMatch.getLoser(), leagueMatch.getWinner());
            appendMatch(playerWins, playerLoss, leagueMatch.getWinner(), leagueMatch.getLoser());
        }

        List<PlayerStanding> leagueStandings = new LinkedList<PlayerStanding>();
        for (Map.Entry<String, LeaguePointsDAO.Points> playerPoints : points.entrySet()) {
            PlayerStanding standing = new PlayerStanding(playerPoints.getKey(), playerPoints.getValue().getPoints(), playerPoints.getValue().getGamesPlayed());
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
        PlayerStanding lastStanding = null;
        for (PlayerStanding leagueStanding : leagueStandings) {
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

    public boolean canPlayRankedGameAgainst(League league, LeagueSerieData season, String playerOne, String playerTwo) {
        Collection<LeagueMatch> playedInSeason = getPlayerMatchesInSerie(league, season, playerOne);
        for (LeagueMatch leagueMatch : playedInSeason) {
            if (playerTwo.equals(leagueMatch.getWinner()) || playerTwo.equals(leagueMatch.getLoser()))
                return false;
        }
        return true;
    }

    private class PointsComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            return o1.getPoints() - o2.getPoints();
        }
    }

    private class GamesPlayedComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            return o1.getGamesPlayed() - o2.getGamesPlayed();
        }
    }

    private class OpponentsWinComparator implements Comparator<PlayerStanding> {
        @Override
        public int compare(PlayerStanding o1, PlayerStanding o2) {
            final float diff = o1.getOpponentWin() - o2.getOpponentWin();
            if (diff < 0)
                return -1;
            if (diff > 0)
                return 1;
            return 0;
        }
    }
}
