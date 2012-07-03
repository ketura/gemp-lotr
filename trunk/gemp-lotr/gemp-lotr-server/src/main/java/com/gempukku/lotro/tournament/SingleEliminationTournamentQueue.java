package com.gempukku.lotro.tournament;

import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SingleEliminationTournamentQueue implements TournamentQueue {
    private int _cost;
    private LotroFormat _lotroFormat;
    private CollectionType _collectionType;
    private String _tournamentQueueName;

    private Map<String, LotroDeck> _playerDecks = new HashMap<String, LotroDeck>();

    private int _playerCap;

    private TournamentService _tournamentService;
    private String _tournamentIdPrefix;
    private int _tournamentIteration;

    public SingleEliminationTournamentQueue(int cost, LotroFormat lotroFormat, CollectionType collectionType, String tournamentIdPrefix, int tournamentIteration, String tournamentQueueName, int playerCap, TournamentService tournamentService) {
        _cost = cost;
        _lotroFormat = lotroFormat;
        _collectionType = collectionType;
        _tournamentQueueName = tournamentQueueName;
        _playerCap = playerCap;
        _tournamentIdPrefix = tournamentIdPrefix;
        _tournamentIteration = tournamentIteration;
        _tournamentService = tournamentService;
    }

    @Override
    public LotroFormat getLotroFormat() {
        return _lotroFormat;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CollectionType getCollectionType() {
        return _collectionType;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTournamentQueueName() {
        return _tournamentQueueName;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public synchronized boolean process(TournamentQueueCallback tournamentQueueCallback) {
        if (_playerDecks.size() == _playerCap) {
            String tournamentId = _tournamentIdPrefix + System.currentTimeMillis();
            String tournamentName = _tournamentQueueName + " - " + _tournamentIteration;

            String parameters = _cost + "," + _lotroFormat.getName() + "," + _collectionType.getCode() + "," + _collectionType.getFullName() + "," + tournamentName;

            _tournamentService.addTournament(_cost, tournamentId, SingleEliminationTournament.class.getName(), parameters, new Date());

            for (Map.Entry<String, LotroDeck> playerDeck : _playerDecks.entrySet())
                _tournamentService.addPlayer(tournamentId, playerDeck.getKey(), playerDeck.getValue());

            tournamentQueueCallback.createTournament(
                    new SingleEliminationTournament(_tournamentService, tournamentId, parameters));
            tournamentQueueCallback.createTournamentQueue(
                    new SingleEliminationTournamentQueue(_cost, _lotroFormat, _collectionType, _tournamentIdPrefix, _tournamentIteration + 1,
                            _tournamentQueueName, _playerCap, _tournamentService));
            return true;
        }
        return false;
    }

    @Override
    public synchronized void joinPlayer(String player, LotroDeck deck) {
        if (!_playerDecks.containsKey(player) && _playerDecks.size() < _playerCap)
            _playerDecks.put(player, deck);
    }

    @Override
    public synchronized int getPlayerCount() {
        return _playerDecks.size();
    }

    @Override
    public synchronized void leavePlayer(String player) {
        _playerDecks.remove(player);
    }

    @Override
    public synchronized boolean isPlayerSignedUp(String player) {
        return _playerDecks.containsKey(player);
    }
}
