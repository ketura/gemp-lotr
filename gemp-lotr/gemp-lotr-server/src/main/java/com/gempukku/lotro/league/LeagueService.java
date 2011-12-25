package com.gempukku.lotro.league;

import com.gempukku.lotro.db.*;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.LeagueMatch;
import com.gempukku.lotro.db.vo.LeagueSerie;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.logic.timing.GameResultListener;

import java.util.*;

public class LeagueService {
    private LeagueDAO _leagueDao;
    private LeagueSerieDAO _leagueSeasonDao;
    private LeaguePointsDAO _leaguePointsDao;
    private LeagueMatchDAO _leagueMatchDao;
    private CollectionDAO _collectionDao;
    private LotroCardBlueprintLibrary _library;

    public LeagueService(LeagueDAO leagueDao, LeagueSerieDAO leagueSeasonDao, LeaguePointsDAO leaguePointsDao, LeagueMatchDAO leagueMatchDao, CollectionDAO collectionDao, LotroCardBlueprintLibrary library) {
        _leagueDao = leagueDao;
        _leagueSeasonDao = leagueSeasonDao;
        _leaguePointsDao = leaguePointsDao;
        _leagueMatchDao = leagueMatchDao;
        _collectionDao = collectionDao;
        _library = library;
    }

    public Set<League> getActiveLeagues() {
        return _leagueDao.getActiveLeagues();
    }

    public MutableCardCollection getLeagueCollection(Player player, League league) {
        final MutableCardCollection collectionForPlayer = _collectionDao.getCollectionForPlayer(player, league.getType());
        if (collectionForPlayer == null) {
            DefaultCardCollection collection = new DefaultCardCollection();

            MutableCardCollection baseCollection = league.getBaseCollection();
            for (CardCollection.Item item : baseCollection.getItems(null, _library)) {
                if (item.getType() == CardCollection.Item.Type.CARD)
                    collection.addCards(item.getBlueprintId(), item.getCount());
                else
                    collection.addPacks(item.getBlueprintId(), item.getCount());
            }
            return collection;
        }
        return collectionForPlayer;
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
                        }
                    });
            gameMediator.sendMessageToPlayers("This is a ranked game in " + league.getName());
        } else {
            gameMediator.sendMessageToPlayers("This is NOT a ranked game in " + league.getName());
        }
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
}
