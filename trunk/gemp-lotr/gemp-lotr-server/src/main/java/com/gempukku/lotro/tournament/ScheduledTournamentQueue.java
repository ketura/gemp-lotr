package com.gempukku.lotro.tournament;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;

import java.util.Date;

public class ScheduledTournamentQueue extends AbstractTournamentQueue implements TournamentQueue {
    private long _startTime;
    private int _minimumPlayers;
    private String _startCondition;
    private TournamentService _tournamentService;
    private String _tournamentName;
    private String _format;
    private CollectionType _collectionType;
    private Tournament.Stage _stage;
    private PairingMechanism _pairingMechanism;
    private TournamentPrizes _prizeScheme;
    private String _scheduledTournamentId;

    public ScheduledTournamentQueue(String scheduledTournamentId, int cost, boolean requiresDeck, TournamentService tournamentService, long startTime,
                                    String tournamentName, String format, CollectionType collectionType, Tournament.Stage stage,
                                    PairingMechanism pairingMechanism, TournamentPrizes prizeScheme, int minimumPlayers) {
        super(cost, requiresDeck);
        _scheduledTournamentId = scheduledTournamentId;
        _tournamentService = tournamentService;
        _startTime = startTime;
        _minimumPlayers = minimumPlayers;
        _startCondition = DateUtils.formatDateWithHour(new Date(_startTime));
        _tournamentName = tournamentName;
        _format = format;
        _collectionType = collectionType;
        _stage = stage;
        _pairingMechanism = pairingMechanism;
        _prizeScheme = prizeScheme;
    }

    @Override
    public int getCost() {
        return _cost;
    }

    @Override
    public String getFormat() {
        return _format;
    }

    @Override
    public CollectionType getCollectionType() {
        return _collectionType;
    }

    @Override
    public String getTournamentQueueName() {
        return _tournamentName;
    }

    @Override
    public String getPrizesDescription() {
        return _prizeScheme.getPrizeDescription();
    }

    @Override
    public String getPairingDescription() {
        return _pairingMechanism.getPlayOffSystem() + ", minimum players: " + _minimumPlayers;
    }

    @Override
    public String getStartCondition() {
        return _startCondition;
    }

    @Override
    public boolean isRequiresDeck() {
        return _requiresDeck;
    }

    @Override
    public synchronized boolean process(TournamentQueueCallback tournamentQueueCallback, CollectionsManager collectionsManager) {
        long now = System.currentTimeMillis();
        if (now > _startTime) {
            if (_players.size() >= _minimumPlayers) {
                
                for (String player : _players)
                    _tournamentService.addPlayer(_scheduledTournamentId, player, _playerDecks.get(player));

                Tournament tournament = _tournamentService.addTournament(_scheduledTournamentId, null, _tournamentName, _format, _collectionType, _stage,
                        _pairingMechanism.getRegistryRepresentation(), _prizeScheme.getRegistryRepresentation(), new Date());
                tournamentQueueCallback.createTournament(tournament);
            } else {
                _tournamentService.updateScheduledTournamentStarted(_scheduledTournamentId);
                leaveAllPlayers(collectionsManager);
            }

            return true;
        }
        return false;
    }
}
