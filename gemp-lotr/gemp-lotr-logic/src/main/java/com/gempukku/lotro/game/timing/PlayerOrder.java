package com.gempukku.lotro.game.timing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerOrder {
    private final List<String> _turnOrder;

    public PlayerOrder(List<String> turnOrder) {
        _turnOrder = turnOrder;
    }

    public String getFirstPlayer() {
        return _turnOrder.get(0);
    }

    public List<String> getAllPlayers() {
        return Collections.unmodifiableList(_turnOrder);
    }

    public PlayOrder getCounterClockwisePlayOrder(String startingPlayerId, boolean looped) {
        int currentPlayerIndex = _turnOrder.indexOf(startingPlayerId);
        List<String> playOrder = new ArrayList<>();
        int nextIndex = currentPlayerIndex;
        do {
            playOrder.add(_turnOrder.get(nextIndex));
            nextIndex--;
            if (nextIndex < 0)
                nextIndex = _turnOrder.size() - 1;
        } while (currentPlayerIndex != nextIndex);
        return new PlayOrder(playOrder, looped);
    }

    public PlayOrder getClockwisePlayOrder(String startingPlayerId, boolean looped) {
        int currentPlayerIndex = _turnOrder.indexOf(startingPlayerId);
        List<String> playOrder = new ArrayList<>();
        int nextIndex = currentPlayerIndex;
        do {
            playOrder.add(_turnOrder.get(nextIndex));
            nextIndex++;
            if (nextIndex == _turnOrder.size())
                nextIndex = 0;
        } while (currentPlayerIndex != nextIndex);
        return new PlayOrder(playOrder, looped);
    }

    public int getPlayerCount() {
        return _turnOrder.size();
    }
}
