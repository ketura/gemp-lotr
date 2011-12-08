package com.gempukku.lotro.league;

import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.LeagueMatchDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.logic.timing.GameResultListener;

import java.util.*;

public class LeagueService {
    private LeagueDAO _leagueDao;
    private LeagueMatchDAO _leagueMatchDao;
    private CollectionDAO _collectionDao;
    private LotroCardBlueprintLibrary _library;

    public LeagueService(DbAccess dbAccess, CollectionDAO collectionDao, LotroCardBlueprintLibrary library) {
        _collectionDao = collectionDao;
        _library = library;
        _leagueDao = new LeagueDAO(dbAccess, library);
        _leagueMatchDao = new LeagueMatchDAO(dbAccess);
    }

    public Set<League> getActiveLeagues() {
        return _leagueDao.getActiveLeagues();
    }

    public MutableCardCollection getLeagueCollection(Player player, League league) {
        final MutableCardCollection collectionForPlayer = _collectionDao.getCollectionForPlayer(player, league.getType());
        if (collectionForPlayer == null) {
            DefaultCardCollection collection = new DefaultCardCollection(_library);

            MutableCardCollection baseCollection = league.getBaseCollection();
            for (CardCollection.Item item : baseCollection.getItems(null)) {
                if (item.getType() == CardCollection.Item.Type.CARD)
                    collection.addCards(item.getBlueprintId(), _library.getLotroCardBlueprint(item.getBlueprintId()), item.getCount());
                else
                    collection.addPacks(item.getBlueprintId(), item.getCount());
            }
            collection.finishedReading();
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

    public LotroFormat getLeagueFormat(League league, Player player) {
        return new LeagueFormat(_library, getLeagueCollection(player, league), true);
    }

    public void leagueGameStarting(final League league, LotroGameMediator gameMediator) {
        final int startDay = getCurrentDate();
        if (isRanked(league, gameMediator, startDay)) {
            gameMediator.addGameResultListener(
                    new GameResultListener() {
                        @Override
                        public void gameFinished(String winnerPlayerId, String winReason, Map<String, String> loserPlayerIdsWithReasons) {
                            _leagueMatchDao.addPlayedMatch(league, winnerPlayerId, loserPlayerIdsWithReasons.keySet().iterator().next(), startDay);
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

    private boolean isRanked(League league, LotroGameMediator gameMediator, int startDate) {
        for (String player : gameMediator.getPlayersPlaying()) {
            if (_leagueMatchDao.getPlayerMatchesPlayedOn(league, player, startDate).size() >= 2)
                return false;
        }
        return true;
    }
}
