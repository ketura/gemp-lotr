package com.gempukku.lotro.server.provider;

import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.collection.DeliveryService;
import com.gempukku.lotro.db.*;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.game.formats.LotroFormatLibrary;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueService;
import com.gempukku.lotro.merchant.MerchantService;
import com.gempukku.lotro.trade.TradeServer;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class ServerProvider implements InjectableProvider<Context, Type> {
    private Injectable<ChatServer> _chatServerInjectable;
    private Injectable<LeagueService> _leagueServiceInjectable;
    private Injectable<MerchantService> _merchantServiceInjectable;
    private Injectable<HallServer> _hallServerInjectable;
    private Injectable<LotroServer> _lotroServerInjectable;
    private Injectable<TradeServer> _tradeServerInjectable;
    private Injectable<CollectionsManager> _collectionsManagerInjectable;
    private Injectable<DeliveryService> _deliveryServiceInjectable;
    private Injectable<LotroFormatLibrary> _lotroFormatLibraryInjectable;

    @Context
    private PlayerDAO _playerDao;
    @Context
    private DeckDAO _deckDao;
    @Context
    private CollectionDAO _collectionDao;
    @Context
    private MerchantDAO _merchantDao;
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueMatchDAO _leagueMatchDao;
    @Context
    private LeaguePointsDAO _leaguePointsDao;
    @Context
    private LeagueParticipationDAO _leagueParticipationDao;
    @Context
    private GameHistoryDAO _gameHistoryDao;
    @Context
    private LotroCardBlueprintLibrary _library;

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(ChatServer.class))
            return getChatServerInjectable();
        if (type.equals(LotroServer.class))
            return getLotroServerInjectable();
        if (type.equals(HallServer.class))
            return getHallServerInjectable();
        if (type.equals(LeagueService.class))
            return getLeagueServiceInjectable();
        if (type.equals(CollectionsManager.class))
            return getCollectionsManagerInjectable();
        if (type.equals(TradeServer.class))
            return getTradeServerInjectable();
        if (type.equals(DeliveryService.class))
            return getDeliveryServiceInjectable();
        if (type.equals(LotroFormatLibrary.class))
            return getLotroFormatLibraryInjectable();
        if (type.equals(MerchantService.class))
            return getMerchantServiceInjectable();
        return null;
    }

    private synchronized Injectable<LotroFormatLibrary> getLotroFormatLibraryInjectable() {
        if (_lotroFormatLibraryInjectable == null) {
            final LotroFormatLibrary lotroFormatLibrary = new LotroFormatLibrary(_library);
            _lotroFormatLibraryInjectable = new Injectable<LotroFormatLibrary>() {
                @Override
                public LotroFormatLibrary getValue() {
                    return lotroFormatLibrary;
                }
            };
        }
        return _lotroFormatLibraryInjectable;
    }

    private synchronized Injectable<LeagueService> getLeagueServiceInjectable() {
        if (_leagueServiceInjectable == null) {
            final LeagueService leagueService = new LeagueService(_leagueDao, _leaguePointsDao, _leagueMatchDao, _leagueParticipationDao, getCollectionsManagerInjectable().getValue());
            _leagueServiceInjectable = new Injectable<LeagueService>() {
                @Override
                public LeagueService getValue() {
                    return leagueService;
                }
            };
        }
        return _leagueServiceInjectable;
    }

    private synchronized Injectable<MerchantService> getMerchantServiceInjectable() {
        if (_merchantServiceInjectable == null) {
            final MerchantService merchantService = new MerchantService(_library, getCollectionsManagerInjectable().getValue(), _merchantDao);
            _merchantServiceInjectable = new Injectable<MerchantService>() {
                @Override
                public MerchantService getValue() {
                    return merchantService;
                }
            };
        }
        return _merchantServiceInjectable;
    }

    private synchronized Injectable<DeliveryService> getDeliveryServiceInjectable() {
        if (_deliveryServiceInjectable == null) {
            final DeliveryService deliveryService = new DeliveryService();
            _deliveryServiceInjectable = new Injectable<DeliveryService>() {
                @Override
                public DeliveryService getValue() {
                    return deliveryService;
                }
            };
        }
        return _deliveryServiceInjectable;
    }

    private synchronized Injectable<HallServer> getHallServerInjectable() {
        if (_hallServerInjectable == null) {
            final HallServer hallServer = new HallServer(getLotroServerInjectable().getValue(), getChatServerInjectable().getValue(), getLeagueServiceInjectable().getValue(), _library,
                    getLotroFormatLibraryInjectable().getValue(), getCollectionsManagerInjectable().getValue(), false);
            hallServer.startServer();
            _hallServerInjectable = new Injectable<HallServer>() {
                @Override
                public HallServer getValue() {
                    return hallServer;
                }
            };
        }
        return _hallServerInjectable;
    }

    private synchronized Injectable<TradeServer> getTradeServerInjectable() {
        if (_tradeServerInjectable == null) {
            final TradeServer tradeServer = new TradeServer(getChatServerInjectable().getValue());
            tradeServer.startServer();
            _tradeServerInjectable = new Injectable<TradeServer>() {
                @Override
                public TradeServer getValue() {
                    return tradeServer;
                }
            };
        }
        return _tradeServerInjectable;
    }

    private synchronized Injectable<LotroServer> getLotroServerInjectable() {
        if (_lotroServerInjectable == null) {
            final LotroServer lotroServer = new LotroServer(_deckDao, _gameHistoryDao, _library, getChatServerInjectable().getValue(), false);
            lotroServer.startServer();
            _lotroServerInjectable = new Injectable<LotroServer>() {
                @Override
                public LotroServer getValue() {
                    return lotroServer;
                }
            };
        }
        return _lotroServerInjectable;
    }

    private synchronized Injectable<ChatServer> getChatServerInjectable() {
        if (_chatServerInjectable == null) {
            final ChatServer chatServer = new ChatServer();
            chatServer.startServer();
            _chatServerInjectable = new Injectable<ChatServer>() {
                @Override
                public ChatServer getValue() {
                    return chatServer;
                }
            };
        }
        return _chatServerInjectable;
    }

    private synchronized Injectable<CollectionsManager> getCollectionsManagerInjectable() {
        if (_collectionsManagerInjectable == null) {
            final CollectionsManager collectionsManager = new CollectionsManager(_playerDao, _collectionDao, getDeliveryServiceInjectable().getValue());
            _collectionsManagerInjectable = new Injectable<CollectionsManager>() {
                @Override
                public CollectionsManager getValue() {
                    return collectionsManager;
                }
            };
        }
        return _collectionsManagerInjectable;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }
}
