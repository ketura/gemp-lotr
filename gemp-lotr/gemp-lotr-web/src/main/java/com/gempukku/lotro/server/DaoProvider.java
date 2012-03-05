package com.gempukku.lotro.server;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.*;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class DaoProvider implements InjectableProvider<Context, Type> {
    private Injectable<PlayerDAO> _playerDaoInjectable;
    private Injectable<CollectionDAO> _collectionDaoInjectable;
    private Injectable<DeckDAO> _deckDaoInjectable;
    private Injectable<GameHistoryDAO> _gameHistoryDAOInjectable;

    private Injectable<LeagueDAO> _leagueDaoInjectable;
    private Injectable<LeagueMatchDAO> _leagueMatchDAOInjectable;
    private Injectable<LeaguePointsDAO> _leaguePointsDAOInjectable;

    private DbAccess _dbAccess;
    private CollectionSerializer _collectionSerializer;

    public DaoProvider() {
        _dbAccess = new DbAccess();
        _collectionSerializer = new CollectionSerializer();
    }

    @Override
    public Injectable getInjectable(ComponentContext ic, Context context, Type type) {
        if (type.equals(PlayerDAO.class))
            return getPlayerDaoSafely();
        else if (type.equals(CollectionDAO.class))
            return getCollectionDaoSafely();
        else if (type.equals(DeckDAO.class))
            return getDeckDaoSafely();
        else if (type.equals(GameHistoryDAO.class))
            return getGameHistoryDaoSafely();
        else if (type.equals(LeagueDAO.class))
            return getLeagueDaoSafely();
        else if (type.equals(LeagueMatchDAO.class))
            return getLeagueMatchDaoSafely();
        else if (type.equals(LeaguePointsDAO.class))
            return getLeaguePointsDaoSafely();
        return null;
    }

    private synchronized Injectable<LeaguePointsDAO> getLeaguePointsDaoSafely() {
        if (_leaguePointsDAOInjectable == null) {
            final LeaguePointsDAO leaguePointsDAO = new LeaguePointsDAO(_dbAccess);
            _leaguePointsDAOInjectable = new Injectable<LeaguePointsDAO>() {
                @Override
                public LeaguePointsDAO getValue() {
                    return leaguePointsDAO;
                }
            };
        }
        return _leaguePointsDAOInjectable;
    }

    private synchronized Injectable<LeagueMatchDAO> getLeagueMatchDaoSafely() {
        if (_leagueMatchDAOInjectable == null) {
            final LeagueMatchDAO leagueMatchDao = new LeagueMatchDAO(_dbAccess);
            _leagueMatchDAOInjectable = new Injectable<LeagueMatchDAO>() {
                @Override
                public LeagueMatchDAO getValue() {
                    return leagueMatchDao;
                }
            };
        }
        return _leagueMatchDAOInjectable;
    }

    private synchronized Injectable<LeagueDAO> getLeagueDaoSafely() {
        if (_leagueDaoInjectable == null) {
            final LeagueDAO leagueDao = new LeagueDAO(_dbAccess, _collectionSerializer);
            _leagueDaoInjectable = new Injectable<LeagueDAO>() {
                @Override
                public LeagueDAO getValue() {
                    return leagueDao;
                }
            };
        }
        return _leagueDaoInjectable;
    }

    private synchronized Injectable<GameHistoryDAO> getGameHistoryDaoSafely() {
        if (_gameHistoryDAOInjectable == null) {
            final GameHistoryDAO gameHistoryDAO = new GameHistoryDAO(_dbAccess);
            _gameHistoryDAOInjectable = new Injectable<GameHistoryDAO>() {
                @Override
                public GameHistoryDAO getValue() {
                    return gameHistoryDAO;
                }
            };
        }
        return _gameHistoryDAOInjectable;
    }

    private synchronized Injectable<DeckDAO> getDeckDaoSafely() {
        if (_deckDaoInjectable == null) {
            final DeckDAO deckDao = new DeckDAO(_dbAccess);
            _deckDaoInjectable = new Injectable<DeckDAO>() {
                @Override
                public DeckDAO getValue() {
                    return deckDao;
                }
            };
        }
        return _deckDaoInjectable;
    }

    private synchronized Injectable<CollectionDAO> getCollectionDaoSafely() {
        if (_collectionDaoInjectable == null) {
            final CollectionDAO collectionDao = new CollectionDAO(_dbAccess, _collectionSerializer);
            _collectionDaoInjectable = new Injectable<CollectionDAO>() {
                @Override
                public CollectionDAO getValue() {
                    return collectionDao;
                }
            };
        }
        return _collectionDaoInjectable;
    }

    private synchronized Injectable<PlayerDAO> getPlayerDaoSafely() {
        if (_playerDaoInjectable == null) {
            final PlayerDAO playerDao = new PlayerDAO(_dbAccess);
            _playerDaoInjectable = new Injectable<PlayerDAO>() {
                @Override
                public PlayerDAO getValue() {
                    return playerDao;
                }
            };
        }
        return _playerDaoInjectable;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }
}
