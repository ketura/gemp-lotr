package com.gempukku.lotro.builder;

import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.collection.TransferDAO;
import com.gempukku.lotro.common.ApplicationConfiguration;
import com.gempukku.lotro.db.*;
import com.gempukku.lotro.draft2.SoloDraftDefinitions;
import com.gempukku.lotro.game.*;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.merchant.MerchantService;
import com.gempukku.lotro.packs.DraftPackStorage;
import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.service.AdminService;
import com.gempukku.lotro.service.LoggedUserHolder;
import com.gempukku.lotro.tournament.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ServerBuilder {
    public static void fillObjectMap(Map<Type, Object> objectMap) {
        objectMap.put(AdventureLibrary.class,
                new DefaultAdventureLibrary());

        objectMap.put(LotroFormatLibrary.class,
                new LotroFormatLibrary(
                        extract(objectMap, AdventureLibrary.class),
                        extract(objectMap, LotroCardBlueprintLibrary.class)));

        objectMap.put(GameHistoryService.class,
                new GameHistoryService(
                        extract(objectMap, GameHistoryDAO.class)));
        objectMap.put(GameRecorder.class,
                new GameRecorder(
                        extract(objectMap, GameHistoryService.class)));

        objectMap.put(CollectionsManager.class,
                new CollectionsManager(
                        extract(objectMap, PlayerDAO.class),
                        extract(objectMap, CollectionDAO.class),
                        extract(objectMap, TransferDAO.class),
                        extract(objectMap, LotroCardBlueprintLibrary.class)));

        objectMap.put(SoloDraftDefinitions.class,
                new SoloDraftDefinitions(
                        extract(objectMap, CollectionsManager.class),
                        extract(objectMap, LotroCardBlueprintLibrary.class),
                        extract(objectMap, LotroFormatLibrary.class),
                        extract(objectMap, CardSets.class).getSetDefinitions()));

        objectMap.put(LeagueService.class,
                new LeagueService(
                        extract(objectMap, LeagueDAO.class),
                        extract(objectMap, LeagueMatchDAO.class),
                        extract(objectMap, LeagueParticipationDAO.class),
                        extract(objectMap, CollectionsManager.class),
                        extract(objectMap, CardSets.class),
                        extract(objectMap, SoloDraftDefinitions.class)));

        objectMap.put(AdminService.class,
                new AdminService(
                        extract(objectMap, PlayerDAO.class),
                        extract(objectMap, IpBanDAO.class),
                        extract(objectMap, LoggedUserHolder.class)
                ));

        TournamentPrizeSchemeRegistry tournamentPrizeSchemeRegistry = new TournamentPrizeSchemeRegistry();
        PairingMechanismRegistry pairingMechanismRegistry = new PairingMechanismRegistry();

        objectMap.put(TournamentService.class,
                new TournamentService(
                        extract(objectMap, CollectionsManager.class),
                        extract(objectMap, PacksStorage.class),
                        new DraftPackStorage(),
                        pairingMechanismRegistry,
                        tournamentPrizeSchemeRegistry,
                        extract(objectMap, TournamentDAO.class),
                        extract(objectMap, TournamentPlayerDAO.class),
                        extract(objectMap, TournamentMatchDAO.class),
                        extract(objectMap, CardSets.class)));

        objectMap.put(MerchantService.class,
                new MerchantService(
                        extract(objectMap, LotroCardBlueprintLibrary.class),
                        extract(objectMap, CollectionsManager.class),
                        extract(objectMap, CardSets.class)));

        objectMap.put(ChatServer.class, new ChatServer(
                extract(objectMap, IgnoreDAO.class),
                extract(objectMap, PlayerDAO.class)));

        objectMap.put(LotroServer.class,
                new LotroServer(
                        extract(objectMap, DeckDAO.class),
                        extract(objectMap, LotroCardBlueprintLibrary.class),
                        extract(objectMap, ChatServer.class),
                        extract(objectMap, GameRecorder.class)));

        objectMap.put(HallServer.class,
                new HallServer(
                        extract(objectMap, IgnoreDAO.class),
                        extract(objectMap, LotroServer.class),
                        extract(objectMap, ChatServer.class),
                        extract(objectMap, LeagueService.class),
                        extract(objectMap, TournamentService.class),
                        extract(objectMap, LotroCardBlueprintLibrary.class),
                        extract(objectMap, LotroFormatLibrary.class),
                        extract(objectMap, CollectionsManager.class),
                        extract(objectMap, AdminService.class),
                        tournamentPrizeSchemeRegistry,
                        pairingMechanismRegistry,
                        extract(objectMap, CardSets.class)
                ));
    }

    private static <T> T extract(Map<Type, Object> objectMap, Class<T> clazz) {
        T result = (T) objectMap.get(clazz);
        if (result == null)
            throw new RuntimeException("Unable to find class " + clazz.getName());
        return result;
    }

    public static void constructObjects(Map<Type, Object> objectMap) {
        extract(objectMap, HallServer.class).startServer();
        extract(objectMap, LotroServer.class).startServer();
        extract(objectMap, ChatServer.class).startServer();
    }

    public static void destroyObjects(Map<Type, Object> objectMap) {
        extract(objectMap, HallServer.class).stopServer();
        extract(objectMap, LotroServer.class).stopServer();
        extract(objectMap, ChatServer.class).stopServer();
    }
}
