package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPhasesGameProcess implements GameProcess {
    private LotroGame _game;
    private PlayOrder _playOrder;

    public ShadowPhasesGameProcess(LotroGame game) {
        _game = game;
        GameState gameState = _game.getGameState();
        _playOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
    }

    @Override
    public void process() {
        _playOrder.getNextPlayer();
    }

    @Override
    public GameProcess getNextProcess() {
        String shadowPlayer = _playOrder.getNextPlayer();
        return new ShadowPhaseOfPlayerGameProcess(_game, _playOrder, shadowPlayer);
    }
}
