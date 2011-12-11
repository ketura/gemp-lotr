package com.gempukku.lotro.server;

import com.gempukku.lotro.db.*;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
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
    private Injectable<LeagueSeasonDAO> _leagueSeasonDAOInjectable;
    private Injectable<LeagueMatchDAO> _leagueMatchDAOInjectable;
    private Injectable<LeaguePointsDAO> _leaguePointsDAOInjectable;

    @Context
    private LotroCardBlueprintLibrary _library;
    private DbAccess _dbAccess;

    public DaoProvider() {
        _dbAccess = new DbAccess();
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
        else if (type.equals(LeagueSeasonDAO.class))
            return getLeagueSeasonDaoSafely();
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

    private synchronized Injectable<LeagueSeasonDAO> getLeagueSeasonDaoSafely() {
        if (_leagueSeasonDAOInjectable == null) {
            final LeagueSeasonDAO leagueSeasonDao = new LeagueSeasonDAO(_dbAccess);
            _leagueSeasonDAOInjectable = new Injectable<LeagueSeasonDAO>() {
                @Override
                public LeagueSeasonDAO getValue() {
                    return leagueSeasonDao;
                }
            };
        }
        return _leagueSeasonDAOInjectable;
    }

    private synchronized Injectable<LeagueDAO> getLeagueDaoSafely() {
        if (_leagueDaoInjectable == null) {
            final LeagueDAO leagueDao = new LeagueDAO(_dbAccess, _library);
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
        if (_gameHistoryDAOInjectable == null)
            _gameHistoryDAOInjectable = new GameHistoryDaoInjectable(new GameHistoryDAO(_dbAccess));
        return _gameHistoryDAOInjectable;
    }

    private synchronized Injectable<DeckDAO> getDeckDaoSafely() {
        if (_deckDaoInjectable == null)
            _deckDaoInjectable = new DeckDaoInjectable(new DeckDAO(_dbAccess));
        return _deckDaoInjectable;
    }

    private synchronized Injectable<CollectionDAO> getCollectionDaoSafely() {
        if (_collectionDaoInjectable == null)
            _collectionDaoInjectable = new CollectionDaoInjectable(new CollectionDAO(_dbAccess, _library));
        return _collectionDaoInjectable;
    }

    private synchronized Injectable<PlayerDAO> getPlayerDaoSafely() {
        if (_playerDaoInjectable == null)
            _playerDaoInjectable = new PlayerDaoInjectable(new PlayerDAO(_dbAccess));
        return _playerDaoInjectable;
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.Singleton;
    }

    private class PlayerDaoInjectable implements Injectable<PlayerDAO> {
        private PlayerDAO _playerDao;

        private PlayerDaoInjectable(PlayerDAO playerDao) {
            _playerDao = playerDao;
        }

        @Override
        public PlayerDAO getValue() {
            return _playerDao;
        }
    }

    private class CollectionDaoInjectable implements Injectable<CollectionDAO> {
        private CollectionDAO _collectionDao;

        private CollectionDaoInjectable(CollectionDAO collectionDao) {
            _collectionDao = collectionDao;
        }

        @Override
        public CollectionDAO getValue() {
            return _collectionDao;
        }
    }

    private class DeckDaoInjectable implements Injectable<DeckDAO> {
        private DeckDAO _deckDao;

        private DeckDaoInjectable(DeckDAO deckDao) {
            _deckDao = deckDao;
        }

        @Override
        public DeckDAO getValue() {
            return _deckDao;
        }
    }

    private class GameHistoryDaoInjectable implements Injectable<GameHistoryDAO> {
        private GameHistoryDAO _gameHistoryDao;

        private GameHistoryDaoInjectable(GameHistoryDAO gameHistoryDao) {
            _gameHistoryDao = gameHistoryDao;
        }

        @Override
        public GameHistoryDAO getValue() {
            return _gameHistoryDao;
        }
    }
}
