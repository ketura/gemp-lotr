package com.gempukku.lotro.tournament;

import com.gempukku.lotro.DateUtils;
import com.gempukku.lotro.collection.CollectionsManager;
import com.gempukku.lotro.db.vo.CollectionType;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.logic.vo.LotroDeck;

import java.util.*;

public class SingleEliminationRecurringQueue implements TournamentQueue {
    private int _cost;
    private String _format;
    private CollectionType _collectionType;
    private String _tournamentQueueName;
    private CollectionType _currencyCollection = new CollectionType("permanent", "My cards");

    private Set<String> _players = new HashSet<String>();
    private Map<String, LotroDeck> _playerDecks = new HashMap<String, LotroDeck>();

    private int _playerCap;

    private TournamentService _tournamentService;
    private String _tournamentIdPrefix;

    private boolean _requiresDeck;

    public SingleEliminationRecurringQueue(int cost, String format, CollectionType collectionType, String tournamentIdPrefix, String tournamentQueueName, int playerCap, boolean requiresDeck, TournamentService tournamentService) {
        _cost = cost;
        _format = format;
        _collectionType = collectionType;
        _tournamentQueueName = tournamentQueueName;
        _playerCap = playerCap;
        _tournamentIdPrefix = tournamentIdPrefix;
        _requiresDeck = requiresDeck;
        _tournamentService = tournamentService;
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
    public boolean isRequiresDeck() {
        return _requiresDeck;
    }

    @Override
    public synchronized boolean process(TournamentQueueCallback tournamentQueueCallback) {
        if (_players.size() == _playerCap) {
            String tournamentId = _tournamentIdPrefix + System.currentTimeMillis();

            String tournamentName = _tournamentQueueName + " - " + DateUtils.getStringDateWithHour();

            for (String player : _players)
                _tournamentService.addPlayer(tournamentId, player, _playerDecks.get(player));

            Tournament tournament = _tournamentService.addTournament(tournamentId, null, tournamentName, _format, _collectionType, Tournament.Stage.PLAYING_GAMES, "singleElimination", new Date());

            tournamentQueueCallback.createTournament(tournament);

            _players.clear();
            _playerDecks.clear();
        }
        return false;
    }

    @Override
    public synchronized void joinPlayer(CollectionsManager collectionsManager, Player player, LotroDeck deck) {
        if (!_players.contains(player.getName()) && _players.size() < _playerCap) {
            if (_cost <= 0 || collectionsManager.removeCurrencyFromPlayerCollection(player, _currencyCollection, _cost)) {
                _players.add(player.getName());
                if (_requiresDeck)
                    _playerDecks.put(player.getName(), deck);
            }
        }
    }

    @Override
    public synchronized int getPlayerCount() {
        return _players.size();
    }

    @Override
    public synchronized void leavePlayer(CollectionsManager collectionsManager, Player player) {
        if (_players.contains(player.getName())) {
            if (_cost > 0)
                collectionsManager.addCurrencyToPlayerCollection(player, "Return for leaving queue", _currencyCollection, _cost);
            _players.remove(player.getName());
            _playerDecks.remove(player.getName());
        }
    }

    @Override
    public synchronized void leaveAllPlayers(CollectionsManager collectionsManager) {
        if (_cost > 0) {
            for (String player : _players)
                collectionsManager.addCurrencyToPlayerCollection(player, "Return for leaving queue", _currencyCollection, _cost);
        }
        _players.clear();
        _playerDecks.clear();
    }

    @Override
    public synchronized boolean isPlayerSignedUp(String player) {
        return _players.contains(player);
    }
}
