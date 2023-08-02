package com.gempukku.lotro.game;

import java.util.List;

public class PlayOrder {
    private final List<String> _playOrder;
    private final boolean _looped;
    private String _lastPlayer;
    private int _nextPlayerIndex;

    public PlayOrder(List<String> playOrder, boolean looped) {
        _playOrder = playOrder;
        _looped = looped;
    }

    public String getFirstPlayer() {
        return _playOrder.get(0);
    }

    public String getLastPlayer() {
        return _lastPlayer;
    }

    public String getNextPlayer() {
        if (_nextPlayerIndex >= getPlayerCount())
            return null;

        String nextPlayer = _playOrder.get(_nextPlayerIndex);
        _nextPlayerIndex++;
        if (_nextPlayerIndex >= getPlayerCount() && _looped)
            _nextPlayerIndex = 0;
        _lastPlayer = nextPlayer;
        return nextPlayer;
    }

    public int getPlayerCount() {
        return _playOrder.size();
    }
}
