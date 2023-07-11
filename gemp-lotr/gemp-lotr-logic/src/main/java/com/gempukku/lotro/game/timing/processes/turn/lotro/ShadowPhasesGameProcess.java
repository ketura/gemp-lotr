package com.gempukku.lotro.game.timing.processes.turn.lotro;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.timing.processes.GameProcess;

public class ShadowPhasesGameProcess implements GameProcess {
    private PlayOrder _playOrder;

    @Override
    public void process(DefaultGame game) {
        GameState gameState = game.getGameState();
        _playOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        _playOrder.getNextPlayer();
    }

    @Override
    public GameProcess getNextProcess() {
        String shadowPlayer = _playOrder.getNextPlayer();
        return new ShadowPhaseOfPlayerGameProcess(_playOrder, shadowPlayer);
    }
}
