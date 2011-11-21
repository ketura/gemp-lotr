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

    public CardCollection getLeagueCollection(Player player, League league) {
        final CardCollection collectionForPlayer = _collectionDao.getCollectionForPlayer(player, league.getType());
        if (collectionForPlayer == null) {
            return league.getBaseCollection();
        }
        return collectionForPlayer;
    }

    public LotroFormat getLeagueFormat(League league) {
        return new LeagueFormat(_library, true);
    }
}
