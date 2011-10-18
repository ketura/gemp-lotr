package com.gempukku.lotro.league;

import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroFormat;

import java.util.Set;

public class LeagueService {
    private LeagueDAO _leagueDao;
    private CollectionDAO _collectionDao;
    private LotroCardBlueprintLibrary _library;

    public LeagueService(DbAccess dbAccess, CollectionDAO collectionDao, LotroCardBlueprintLibrary library) {
        _collectionDao = collectionDao;
        _library = library;
        _leagueDao = new LeagueDAO(dbAccess, library);
    }

    public Set<League> getActiveLeagues() {
        return _leagueDao.getActiveLeagues();
    }

    public CardCollection getLeagueCollection(Player player, String type) {
        final CardCollection collectionForPlayer = _collectionDao.getCollectionForPlayer(player, type);
        if (collectionForPlayer == null) {
            final League leagueByType = getLeagueByType(type);
            if (leagueByType != null)
                return leagueByType.getBaseCollection();
        }
        return collectionForPlayer;
    }

    private League getLeagueByType(String type) {
        for (League league : getActiveLeagues()) {
            if (league.getType().equals(type))
                return league;
        }
        return null;
    }

    public LotroFormat getLeagueFormat(String type) {
        return new LeagueFormat(_library, true);
    }
}
