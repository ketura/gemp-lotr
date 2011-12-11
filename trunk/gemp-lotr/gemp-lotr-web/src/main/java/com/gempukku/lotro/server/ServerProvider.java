package com.gempukku.lotro.server;

import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.*;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.game.LotroServer;
import com.gempukku.lotro.hall.HallServer;
import com.gempukku.lotro.league.LeagueService;
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

    @Context
    private DeckDAO _deckDao;
    @Context
    private LeagueDAO _leagueDao;
    @Context
    private LeagueSeasonDAO _leagueSeasonDao;
    @Context
    private LeagueMatchDAO _leagueMatchDao;
    @Context
    private LeaguePointsDAO _leaguePointsDao;
    @Context
    private CollectionDAO _collectionDao;
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
        return null;
    }

    private synchronized Injectable<LeagueService> getLeagueServiceInjectable() {
        if (_leagueServerInjectable == null) {
            final LeagueService leagueService = new LeagueService(_leagueDao, _leagueSeasonDao, _leaguePointsDao, _leagueMatchDao, _collectionDao, _library);
            _leagueServerInjectable = new Injectable<LeagueService>() {
                @Override
                public LeagueService getValue() {
                    return leagueService;
                }
            };
        }
        return _leagueServerInjectable;
    }

    private synchronized Injectable<HallServer> getHallServerInjectable() {
        if (_hallServerInjectable == null) {
            final HallServer hallServer = new HallServer(getLotroServerInjectable().getValue(), getChatServerInjectable().getValue(), getLeagueServiceInjectable().getValue(), _library, false);
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

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }
}
