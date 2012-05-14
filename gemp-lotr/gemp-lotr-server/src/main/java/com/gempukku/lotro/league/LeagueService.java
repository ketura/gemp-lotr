package com.gempukku.lotro.league;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.competitive.PlayerStanding;
import com.gempukku.lotro.competitive.StandingsProducer;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.LeagueParticipationDAO;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatchResult;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LeagueService {
    private LeagueDAO _leagueDao;

    // Cached on this layer
    private CachedLeagueMatchDAO _leagueMatchDao;
    private CachedLeagueParticipationDAO _leagueParticipationDAO;

    private CollectionsManager _collectionsManager;

    private Map<String, List<PlayerStanding>> _leagueStandings = new ConcurrentHashMap<String, List<PlayerStanding>>();
    private Map<String, List<PlayerStanding>> _leagueSerieStandings = new ConcurrentHashMap<String, List<PlayerStanding>>();

    private int _activeLeaguesLoadedDate;
    private List<League> _activeLeagues;

    public LeagueService(LeagueDAO leagueDao, LeagueMatchDAO leagueMatchDao,
                         LeagueParticipationDAO leagueParticipationDAO, CollectionsManager collectionsManager) {
        _leagueDao = leagueDao;
        _leagueMatchDao = new CachedLeagueMatchDAO(leagueMatchDao);
        _leagueParticipationDAO = new CachedLeagueParticipationDAO(leagueParticipationDAO);
        _collectionsManager = collectionsManager;
    }

    public void clearCache() {
        _leagueSerieStandings.clear();
        _leagueStandings.clear();
        _activeLeaguesLoadedDate = 0;

        _leagueMatchDao.clearCache();
        _leagueParticipationDAO.clearCache();
    }

    private synchronized void ensureLoadedCurrentLeagues() {
        int currentDate = DateUtils.getCurrentDate();
        if (currentDate != _activeLeaguesLoadedDate) {
            _leagueMatchDao.clearCache();
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
        return _leagueParticipationDAO.getUsersParticipating(league.getType()).contains(player.getName());
    }

    public boolean playerJoinsLeague(League league, Player player) {
        if (isPlayerInLeague(league, player))
            return false;
        int cost = league.getCost();
        if (_collectionsManager.removeCurrencyFromPlayerCollection(player, new CollectionType("permanent", "My cards"), cost)) {
            _leagueParticipationDAO.userJoinsLeague(league.getType(), player);
            league.getLeagueData().joinLeague(_collectionsManager, player, DateUtils.getCurrentDate());

            _leagueStandings.remove(LeagueMapKeys.getLeagueMapKey(league));

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
        _leagueMatchDao.addPlayedMatch(league.getType(), serie.getName(), winner, loser);

        _leagueStandings.remove(LeagueMapKeys.getLeagueMapKey(league));
        _leagueSerieStandings.remove(LeagueMapKeys.getLeagueSerieMapKey(league, serie));

        awardPrizesToPlayer(league, serie, winner, true);
        awardPrizesToPlayer(league, serie, loser, false);
    }

    private void awardPrizesToPlayer(League league, LeagueSerieData serie, String player, boolean winner) {
        int count = 0;
        Collection<LeagueMatchResult> playerMatchesPlayedOn = getPlayerMatchesInSerie(league, serie, player);
        for (LeagueMatchResult leagueMatch : playerMatchesPlayedOn) {
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

    private Collection<LeagueMatchResult> getPlayerMatchesInSerie(League league, LeagueSerieData serie, String player) {
        final Collection<LeagueMatchResult> allMatches = _leagueMatchDao.getLeagueMatches(league.getType());
        Set<LeagueMatchResult> result = new HashSet<LeagueMatchResult>();
        for (LeagueMatchResult match : allMatches) {
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
        final Collection<String> playersParticipating = _leagueParticipationDAO.getUsersParticipating(league.getType());
        final Collection<LeagueMatchResult> matches = _leagueMatchDao.getLeagueMatches(league.getType());

        Set<LeagueMatchResult> matchesInSerie = new HashSet<LeagueMatchResult>();
        for (LeagueMatchResult match : matches) {
            if (match.getSerieName().equals(leagueSerie.getName()))
                matchesInSerie.add(match);
        }

        return createStandingsForMatchesAndPoints(playersParticipating, matchesInSerie);
    }

    private List<PlayerStanding> createLeagueStandings(League league) {
        final Collection<String> playersParticipating = _leagueParticipationDAO.getUsersParticipating(league.getType());
        final Collection<LeagueMatchResult> matches = _leagueMatchDao.getLeagueMatches(league.getType());

        return createStandingsForMatchesAndPoints(playersParticipating, matches);
    }

    private List<PlayerStanding> createStandingsForMatchesAndPoints(Collection<String> playersParticipating, Collection<LeagueMatchResult> matches) {
        return StandingsProducer.produceStandings(playersParticipating, matches, 2, 1, Collections.<String>emptySet());
    }

    public boolean canPlayRankedGame(League league, LeagueSerieData season, String player) {
        int maxMatches = season.getMaxMatches();
        Collection<LeagueMatchResult> playedInSeason = getPlayerMatchesInSerie(league, season, player);
        if (playedInSeason.size() >= maxMatches)
            return false;
        return true;
    }

    public boolean canPlayRankedGameAgainst(League league, LeagueSerieData season, String playerOne, String playerTwo) {
        Collection<LeagueMatchResult> playedInSeason = getPlayerMatchesInSerie(league, season, playerOne);
        for (LeagueMatchResult leagueMatch : playedInSeason) {
            if (playerTwo.equals(leagueMatch.getWinner()) || playerTwo.equals(leagueMatch.getLoser()))
                return false;
        }
        return true;
    }
}
