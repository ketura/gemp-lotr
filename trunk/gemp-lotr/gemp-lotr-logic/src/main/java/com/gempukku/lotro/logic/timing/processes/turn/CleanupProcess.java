package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class CleanupProcess implements GameProcess {
    private LotroGame _game;

    public CleanupProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process(LotroGame game) {
        _game.getGameState().stopAffectingCardsForCurrentPlayer();
        _game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfTurnGameProcess(_game);
    }
}
