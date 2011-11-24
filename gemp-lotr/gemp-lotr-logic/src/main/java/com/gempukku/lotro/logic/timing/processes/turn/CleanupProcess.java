package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class CleanupProcess implements GameProcess {
    @Override
    public void process(LotroGame game) {
        game.getGameState().stopAffectingCardsForCurrentPlayer();
        game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfTurnGameProcess();
    }
}
