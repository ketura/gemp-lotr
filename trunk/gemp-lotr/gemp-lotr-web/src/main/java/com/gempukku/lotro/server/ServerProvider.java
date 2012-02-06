package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.collection.DeliveryService;
import com.gempukku.lotro.db.*;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueService;
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
    private Injectable<LeagueService> _leagueServerInjectable;
    private Injectable<HallServer> _hallServerInjectable;
    private Injectable<LotroServer> _lotroServerInjectable;
    private Injectable<TradeServer> _tradeServerInjectable;
    private Injectable<CollectionsManager> _collectionsManagerInjectable;
    private Injectable<DeliveryService> _deliveryServiceInjectable;

    @Context
    private PlayerDAO _playerDao;
    @Context
    private DeckDAO _deckDao;
    @Context
    private CollectionDAO _collectionDao;
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueSerieDAO _leagueSeasonDao;
    @Context
    private LeagueMatchDAO _leagueMatchDao;
    @Context
    private LeaguePointsDAO _leaguePointsDao;
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
        return null;
    }

    private synchronized Injectable<LeagueService> getLeagueServiceInjectable() {
        if (_leagueServerInjectable == null) {
            final LeagueService leagueService = new LeagueService(_leagueDao, _leagueSeasonDao, _leaguePointsDao, _leagueMatchDao, getCollectionsManagerInjectable().getValue());
            _leagueServerInjectable = new Injectable<LeagueService>() {
                @Override
                public LeagueService getValue() {
                    return leagueService;
                }
            };
        }
        return _leagueServerInjectable;
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
            final HallServer hallServer = new HallServer(getLotroServerInjectable().getValue(), getChatServerInjectable().getValue(), getLeagueServiceInjectable().getValue(), _library, getCollectionsManagerInjectable().getValue(), false);
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
