package com.gempukku.lotro.tournament;

import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class AbstractTournamentQueue implements TournamentQueue {
    protected int _cost;
    protected Queue<String> _players = new LinkedList<String>();
    protected Map<String, LotroDeck> _playerDecks = new HashMap<String, LotroDeck>();
    protected boolean _requiresDeck;
    
    private CollectionType _currencyCollection = CollectionType.MY_CARDS;

    public AbstractTournamentQueue(int cost, boolean requiresDeck) {
        _cost = cost;
        _requiresDeck = requiresDeck;
    }

    @Override
    public synchronized void joinPlayer(CollectionsManager collectionsManager, Player player, LotroDeck deck) {
        if (!_players.contains(player.getName())) {
            if (_cost <= 0 || collectionsManager.removeCurrencyFromPlayerCollection("Joined "+getTournamentQueueName()+" queue", player, _currencyCollection, _cost)) {
                _players.add(player.getName());
                if (_requiresDeck)
                    _playerDecks.put(player.getName(), deck);
            }
        }
    }

    @Override
    public synchronized void leavePlayer(CollectionsManager collectionsManager, Player player) {
        if (_players.contains(player.getName())) {
            if (_cost > 0)
                collectionsManager.addCurrencyToPlayerCollection(true, "Return for leaving "+getTournamentQueueName()+" queue", player, _currencyCollection, _cost);
            _players.remove(player.getName());
            _playerDecks.remove(player.getName());
        }
    }

    @Override
    public synchronized void leaveAllPlayers(CollectionsManager collectionsManager) {
        if (_cost > 0) {
            for (String player : _players)
                collectionsManager.addCurrencyToPlayerCollection(false, "Return for leaving "+getTournamentQueueName()+" queue", player, _currencyCollection, _cost);
        }
        _players.clear();
        _playerDecks.clear();
    }

    @Override
    public synchronized int getPlayerCount() {
        return _players.size();
    }

    @Override
    public synchronized boolean isPlayerSignedUp(String player) {
        return _players.contains(player);
    }
}
