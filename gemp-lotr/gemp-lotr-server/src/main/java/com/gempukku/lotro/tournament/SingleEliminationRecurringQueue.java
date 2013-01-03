package com.gempukku.lotro.tournament;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;

import java.util.Date;

public class SingleEliminationRecurringQueue extends AbstractTournamentQueue implements TournamentQueue {
    private String _format;
    private CollectionType _collectionType;
    private String _tournamentQueueName;

    private int _playerCap;

    private TournamentService _tournamentService;
    private TournamentPrizes _tournamentPrizes;
    private String _tournamentIdPrefix;

    public SingleEliminationRecurringQueue(int cost, String format, CollectionType collectionType, String tournamentIdPrefix,
                                           String tournamentQueueName, int playerCap, boolean requiresDeck,
                                           TournamentService tournamentService, TournamentPrizes tournamentPrizes) {
        super(cost, requiresDeck);
        _format = format;
        _collectionType = collectionType;
        _tournamentQueueName = tournamentQueueName;
        _playerCap = playerCap;
        _tournamentIdPrefix = tournamentIdPrefix;
        _tournamentService = tournamentService;
        _tournamentPrizes = tournamentPrizes;
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
        return _tournamentQueueName;
    }

    @Override
    public String getPrizesDescription() {
        return _tournamentPrizes.getPrizeDescription();
    }

    @Override
    public String getPairingDescription() {
        return "Single elimination";
    }

    @Override
    public String getStartCondition() {
        return "When "+_playerCap+" players join";
    }

    @Override
    public boolean isRequiresDeck() {
        return _requiresDeck;
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

}
