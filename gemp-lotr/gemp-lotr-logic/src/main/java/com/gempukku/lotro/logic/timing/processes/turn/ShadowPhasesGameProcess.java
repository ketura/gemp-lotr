package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPhasesGameProcess implements GameProcess {
    private PlayOrder _playOrder;

    @Override
    public void process(LotroGame game) {
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
