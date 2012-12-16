package com.gempukku.lotro.server.provider;

import com.gempukku.lotro.cache.CacheManager;
import com.gempukku.lotro.collection.CachedCollectionDAO;
import com.gempukku.lotro.collection.CachedTransferDAO;
import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.collection.TransferDAO;
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

        DbMerchantDAO dbMerchantDao = new DbMerchantDAO(dbAccess);
        CachedMerchantDAO merchantDao = new CachedMerchantDAO(dbMerchantDao);
        objectMap.put(MerchantDAO.class, merchantDao);

        objectMap.put(LeagueDAO.class, new DbLeagueDAO(dbAccess));
        objectMap.put(GameHistoryDAO.class, new DbGameHistoryDAO(dbAccess));

        DbDeckDAO dbDeckDao = new DbDeckDAO(dbAccess, library);
        CachedDeckDAO deckDao = new CachedDeckDAO(dbDeckDao);
        objectMap.put(DeckDAO.class, deckDao);

        DbCollectionDAO dbCollectionDao = new DbCollectionDAO(dbAccess, collectionSerializer);
        CachedCollectionDAO collectionDao = new CachedCollectionDAO(dbCollectionDao);
        objectMap.put(CollectionDAO.class, collectionDao);

        DbPlayerDAO dbPlayerDao = new DbPlayerDAO(dbAccess);
        CachedPlayerDAO playerDao = new CachedPlayerDAO(dbPlayerDao);
        objectMap.put(PlayerDAO.class, playerDao);
        
        DbTransferDAO dbTransferDao = new DbTransferDAO(dbAccess);
        CachedTransferDAO transferDao = new CachedTransferDAO(dbTransferDao);
        objectMap.put(TransferDAO.class, transferDao);

        CacheManager cacheManager = new CacheManager();
        cacheManager.addCache(merchantDao);
        cacheManager.addCache(deckDao);
        cacheManager.addCache(collectionDao);
        cacheManager.addCache(playerDao);
        cacheManager.addCache(transferDao);
        objectMap.put(CacheManager.class, cacheManager);
    }
}
