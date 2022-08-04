package com.gempukku.lotro.tournament;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;

import java.util.Date;

public class ImmediateRecurringQueue extends AbstractTournamentQueue implements TournamentQueue {
    private final String _tournamentQueueName;
    private final int _playerCap;
    private final TournamentService _tournamentService;
    private final String _tournamentIdPrefix;

    public ImmediateRecurringQueue(int cost, String format, CollectionType collectionType, String tournamentIdPrefix,
                                           String tournamentQueueName, int playerCap, boolean requiresDeck,
                                           TournamentService tournamentService, TournamentPrizes tournamentPrizes, PairingMechanism pairingMechanism) {
        super(cost, requiresDeck, collectionType, tournamentPrizes, pairingMechanism, format);
        _tournamentQueueName = tournamentQueueName;
        _playerCap = playerCap;
        _tournamentIdPrefix = tournamentIdPrefix;
        _tournamentService = tournamentService;
    }

    @Override
    public String getTournamentQueueName() {
        return _tournamentQueueName;
    }

    @Override
    public String getStartCondition() {
        return "When "+_playerCap+" players join";
    }

    @Override
    public synchronized boolean process(TournamentQueueCallback tournamentQueueCallback, CollectionsManager collectionsManager) {
        if (_players.size() >= _playerCap) {
            String tournamentId = _tournamentIdPrefix + System.currentTimeMillis();

            String tournamentName = _tournamentQueueName + " - " + DateUtils.getStringDateWithHour();

            for (int i=0; i<_playerCap; i++) {
                String player = _players.poll();
                _tournamentService.addPlayer(tournamentId, player, _playerDecks.get(player));
                _playerDecks.remove(player);
            }

            Tournament tournament = _tournamentService.addTournament(tournamentId, null, tournamentName, _format, _collectionType, Tournament.Stage.PLAYING_GAMES, "singleElimination",
                    _tournamentPrizes.getRegistryRepresentation(), new Date());

            tournamentQueueCallback.createTournament(tournament);
        }
        return false;
    }

    @Override
    public boolean isJoinable() {
        return true;
    }
}
