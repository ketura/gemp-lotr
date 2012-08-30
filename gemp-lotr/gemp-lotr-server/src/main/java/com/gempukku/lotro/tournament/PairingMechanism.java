package com.gempukku.lotro.tournament;

import com.gempukku.lotro.competitive.PlayerStanding;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PairingMechanism {
    public boolean shouldDropLoser();

    public boolean isFinished(int round, Set<String> players, Set<String> droppedPlayers);

    public boolean pairPlayers(int round, Set<String> players, Set<String> droppedPlayers, List<PlayerStanding> currentStandings, Map<String, String> pairingResults, Set<String> byeResults);
}
