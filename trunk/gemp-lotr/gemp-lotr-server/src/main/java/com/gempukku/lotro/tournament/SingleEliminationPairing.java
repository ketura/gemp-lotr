package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;

import java.util.*;

public class SingleEliminationPairing implements PairingMechanism {
    @Override
    public boolean isFinished(int round, Set<String> players, Set<String> droppedPlayers) {
        return players.size() - droppedPlayers.size() < 2;
    }

    @Override
    public boolean pairPlayers(int round, Set<String> players, Set<String> droppedPlayers, List<PlayerStanding> currentStandings, Map<String, String> pairingResults, Set<String> byeResults) {
        if (isFinished(round, players, droppedPlayers))
            return true;

        Set<String> playersInContention = new HashSet<String>(players);
        playersInContention.removeAll(droppedPlayers);

        List<String> playersRandomized = new ArrayList<String>(playersInContention);
        Collections.shuffle(playersRandomized);

        Iterator<String> playerIterator = playersRandomized.iterator();
        while (playerIterator.hasNext()) {
            String playerOne = playerIterator.next();
            if (playerIterator.hasNext()) {
                String playerTwo = playerIterator.next();
                pairingResults.put(playerOne, playerTwo);
            } else {
                byeResults.add(playerOne);
            }
        }

        return false;
    }

    @Override
    public boolean shouldDropLoser() {
        return true;
    }
}
