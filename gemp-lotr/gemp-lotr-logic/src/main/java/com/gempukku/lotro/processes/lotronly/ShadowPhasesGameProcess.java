package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.processes.GameProcess;

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
