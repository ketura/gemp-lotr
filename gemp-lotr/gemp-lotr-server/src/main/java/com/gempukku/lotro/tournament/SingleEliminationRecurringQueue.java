package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SingleEliminationRecurringQueue implements TournamentQueue {
    private int _cost;
    private LotroFormat _lotroFormat;
    private CollectionType _collectionType;
    private String _tournamentQueueName;
    private CollectionType _currencyCollection = new CollectionType("permanent", "My cards");

    private Map<String, LotroDeck> _playerDecks = new HashMap<String, LotroDeck>();

    private int _playerCap;

    private TournamentService _tournamentService;
    private String _tournamentIdPrefix;
    private int _tournamentIteration;

    public SingleEliminationRecurringQueue(int cost, LotroFormat lotroFormat, CollectionType collectionType, String tournamentIdPrefix, int tournamentIteration, String tournamentQueueName, int playerCap, TournamentService tournamentService) {
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
        return _lotroFormat;
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
    public synchronized boolean process(TournamentQueueCallback tournamentQueueCallback) {
        if (_playerDecks.size() == _playerCap) {
            String tournamentId = _tournamentIdPrefix + System.currentTimeMillis();

            String tournamentName = _tournamentQueueName + " - " + _tournamentIteration;
            _tournamentIteration++;

            String parameters = _lotroFormat.getName() + "," + _collectionType.getCode() + "," + _collectionType.getFullName() + "," + tournamentName;

            _tournamentService.addTournament(_cost, tournamentId, SingleEliminationTournament.class.getName(), parameters, new Date());

            for (Map.Entry<String, LotroDeck> playerDeck : _playerDecks.entrySet())
                _tournamentService.addPlayer(tournamentId, playerDeck.getKey(), playerDeck.getValue());

            tournamentQueueCallback.createTournament(
                    new SingleEliminationTournament(_tournamentService, tournamentId, parameters));

            _playerDecks.clear();
        }
        return false;
    }

    @Override
    public synchronized void joinPlayer(CollectionsManager collectionsManager, Player player, LotroDeck deck) {
        if (!_playerDecks.containsKey(player.getName()) && _playerDecks.size() < _playerCap) {
            if (_cost <= 0 || collectionsManager.removeCurrencyFromPlayerCollection(player, _currencyCollection, _cost))
                _playerDecks.put(player.getName(), deck);
        }
    }

    @Override
    public synchronized int getPlayerCount() {
        return _playerDecks.size();
    }

    @Override
    public synchronized void leavePlayer(CollectionsManager collectionsManager, Player player) {
        if (_playerDecks.containsKey(player.getName())) {
            if (_cost > 0)
                collectionsManager.addCurrencyToPlayerCollection(player, _currencyCollection, _cost);
            _playerDecks.remove(player.getName());
        }
    }

    @Override
    public synchronized boolean isPlayerSignedUp(String player) {
        return _playerDecks.containsKey(player);
    }
}
