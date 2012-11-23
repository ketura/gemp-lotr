package com.gempukku.lotro.server.provider;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.*;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.tournament.TournamentDAO;
import com.gempukku.lotro.tournament.TournamentMatchDAO;
import com.gempukku.lotro.tournament.TournamentPlayerDAO;

import java.lang.reflect.Type;
import java.util.Map;

public class DaoBuilder {
    public static void fillObjectMap(Map<Type, Object> objectMap) {
        DbAccess dbAccess = new DbAccess();
        CollectionSerializer collectionSerializer = new CollectionSerializer();

        LotroCardBlueprintLibrary library = new LotroCardBlueprintLibrary();
        objectMap.put(LotroCardBlueprintLibrary.class, library);
        objectMap.put(LeagueParticipationDAO.class, new DbLeagueParticipationDAO(dbAccess));
        objectMap.put(LeagueMatchDAO.class, new DbLeagueMatchDAO(dbAccess));
        objectMap.put(TournamentDAO.class, new DbTournamentDAO(dbAccess));
        objectMap.put(TournamentPlayerDAO.class, new DbTournamentPlayerDAO(dbAccess));
        objectMap.put(TournamentMatchDAO.class, new DbTournamentMatchDAO(dbAccess));
        objectMap.put(MerchantDAO.class, new DbMerchantDAO(dbAccess));
        objectMap.put(LeagueDAO.class, new LeagueDAO(dbAccess));
        objectMap.put(GameHistoryDAO.class, new GameHistoryDAO(dbAccess));
        objectMap.put(DeckDAO.class, new DeckDAO(dbAccess, library));
        objectMap.put(CollectionDAO.class, new DbCollectionDAO(dbAccess, collectionSerializer));
        objectMap.put(PlayerDAO.class, new PlayerDAO(dbAccess));
    }
}
