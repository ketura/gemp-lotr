package com.gempukku.lotro.server.provider;

import com.gempukku.lotro.collection.CollectionSerializer;
import com.gempukku.lotro.db.*;
import com.gempukku.lotro.game.LotroCardBlueprintLibrary;
import com.gempukku.lotro.tournament.TournamentDAO;
import com.gempukku.lotro.tournament.TournamentMatchDAO;
import com.gempukku.lotro.tournament.TournamentPlayerDAO;
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
    private Injectable<MerchantDAO> _merchantDaoInjectable;
    private Injectable<DeckDAO> _deckDaoInjectable;
    private Injectable<GameHistoryDAO> _gameHistoryDAOInjectable;

    private Injectable<LeagueDAO> _leagueDaoInjectable;
    private Injectable<LeagueMatchDAO> _leagueMatchDAOInjectable;
    private Injectable<LeagueParticipationDAO> _leagueParticipationDAOInjectable;

    private Injectable<TournamentDAO> _tournamentDAOInjectable;
    private Injectable<TournamentPlayerDAO> _tournamentPlayerDAOInjectable;
    private Injectable<TournamentMatchDAO> _tournamentMatchDAOInjectable;

    private DbAccess _dbAccess;
    private CollectionSerializer _collectionSerializer;

    private LotroCardBlueprintLibrary _library;

    public DaoProvider() {
        _dbAccess = new DbAccess();
        _collectionSerializer = new CollectionSerializer();

        _library = new LotroCardBlueprintLibrary();
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
        else if (type.equals(LeagueParticipationDAO.class))
            return getLeagueParticipationDaoSafely();
        else if (type.equals(MerchantDAO.class))
            return getMerchantDaoSafely();
        else if (type.equals(TournamentDAO.class))
            return getTournamentDaoSafely();
        else if (type.equals(TournamentPlayerDAO.class))
            return getTournamentPlayerDaoSafely();
        else if (type.equals(TournamentMatchDAO.class))
            return getTournamentMatchDaoSafely();
        return null;
    }

    private synchronized Injectable<LeagueParticipationDAO> getLeagueParticipationDaoSafely() {
        if (_leagueParticipationDAOInjectable == null) {
            final LeagueParticipationDAO leagueParticipationDAO = new DbLeagueParticipationDAO(_dbAccess);
            _leagueParticipationDAOInjectable = new Injectable<LeagueParticipationDAO>() {
                @Override
                public LeagueParticipationDAO getValue() {
                    return leagueParticipationDAO;
                }
            };
        }
        return _leagueParticipationDAOInjectable;
    }

    private synchronized Injectable<LeagueMatchDAO> getLeagueMatchDaoSafely() {
        if (_leagueMatchDAOInjectable == null) {
            final LeagueMatchDAO leagueMatchDao = new DbLeagueMatchDAO(_dbAccess);
            _leagueMatchDAOInjectable = new Injectable<LeagueMatchDAO>() {
                @Override
                public LeagueMatchDAO getValue() {
                    return leagueMatchDao;
                }
            };
        }
        return _leagueMatchDAOInjectable;
    }

    private synchronized Injectable<TournamentDAO> getTournamentDaoSafely() {
        if (_tournamentDAOInjectable == null) {
            final DbTournamentDAO tournamentDAO = new DbTournamentDAO(_dbAccess);
            _tournamentDAOInjectable = new Injectable<TournamentDAO>() {
                @Override
                public TournamentDAO getValue() {
                    return tournamentDAO;
                }
            };
        }
        return _tournamentDAOInjectable;
    }

    private synchronized Injectable<TournamentPlayerDAO> getTournamentPlayerDaoSafely() {
        if (_tournamentPlayerDAOInjectable == null) {
            final DbTournamentPlayerDAO tournamentPlayerDAO = new DbTournamentPlayerDAO(_dbAccess);
            _tournamentPlayerDAOInjectable = new Injectable<TournamentPlayerDAO>() {
                @Override
                public TournamentPlayerDAO getValue() {
                    return tournamentPlayerDAO;
                }
            };
        }
        return _tournamentPlayerDAOInjectable;
    }

    private synchronized Injectable<TournamentMatchDAO> getTournamentMatchDaoSafely() {
        if (_tournamentMatchDAOInjectable == null) {
            final DbTournamentMatchDAO tournamentMatchDAO = new DbTournamentMatchDAO(_dbAccess);
            _tournamentMatchDAOInjectable = new Injectable<TournamentMatchDAO>() {
                @Override
                public TournamentMatchDAO getValue() {
                    return tournamentMatchDAO;
                }
            };
        }
        return _tournamentMatchDAOInjectable;
    }

    private synchronized Injectable<MerchantDAO> getMerchantDaoSafely() {
        if (_merchantDaoInjectable == null) {
            final MerchantDAO merchantDAO = new DbMerchantDAO(_dbAccess);
            _merchantDaoInjectable = new Injectable<MerchantDAO>() {
                @Override
                public MerchantDAO getValue() {
                    return merchantDAO;
                }
            };
        }
        return _merchantDaoInjectable;
    }

    private synchronized Injectable<LeagueDAO> getLeagueDaoSafely() {
        if (_leagueDaoInjectable == null) {
            final LeagueDAO leagueDao = new LeagueDAO(_dbAccess);
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
            final DeckDAO deckDao = new DeckDAO(_dbAccess, _library);
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
