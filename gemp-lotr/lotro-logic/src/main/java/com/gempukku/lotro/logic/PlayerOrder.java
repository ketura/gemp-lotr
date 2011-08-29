package com.gempukku.lotro.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerOrder {
    private List<String> _turnOrder;

    public PlayerOrder(List<String> turnOrder) {
        _turnOrder = turnOrder;
    }

    public List<String> getAllPlayers() {
        return Collections.unmodifiableList(_turnOrder);
    }

    public PlayOrder getCounterClockwisePlayOrder(String startingPlayerId, boolean looped) {
        int currentPlayerIndex = _turnOrder.indexOf(startingPlayerId);
        List<String> playOrder = new ArrayList<String>();
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
        List<String> playOrder = new ArrayList<String>();
        int nextIndex = currentPlayerIndex;
        do {
            playOrder.add(_turnOrder.get(nextIndex));
            nextIndex++;
            if (nextIndex == _turnOrder.size())
                nextIndex = 0;
        } while (currentPlayerIndex != nextIndex);
        return new PlayOrder(playOrder, looped);
    }
}
