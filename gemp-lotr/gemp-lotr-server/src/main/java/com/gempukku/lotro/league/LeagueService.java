package com.gempukku.lotro.league;

import com.gempukku.lotro.db.CollectionDAO;
import com.gempukku.lotro.db.DbAccess;
import com.gempukku.lotro.db.LeagueDAO;
import com.gempukku.lotro.db.vo.League;
import com.gempukku.lotro.db.vo.Player;
import com.gempukku.lotro.game.*;

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

    public MutableCardCollection getLeagueCollection(Player player, League league) {
        final MutableCardCollection collectionForPlayer = _collectionDao.getCollectionForPlayer(player, league.getType());
        if (collectionForPlayer == null) {
            MutableCardCollection collection = new DefaultCardCollection(_library);

            MutableCardCollection baseCollection = league.getBaseCollection();
            for (CardCollection.Item item : baseCollection.getItems(null)) {
                if (item.getType() == CardCollection.Item.Type.CARD)
                    collection.addCards(item.getBlueprintId(), _library.getLotroCardBlueprint(item.getBlueprintId()), item.getCount());
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

    public LotroFormat getLeagueFormat(League league, Player player) {
        return new LeagueFormat(_library, getLeagueCollection(player, league), true);
    }
}
