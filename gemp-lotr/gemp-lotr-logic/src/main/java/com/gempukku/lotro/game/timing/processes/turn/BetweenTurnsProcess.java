package com.gempukku.lotro.game.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.turn.tribbles.TribblesStartOfTurnGameProcess;

public class BetweenTurnsProcess implements GameProcess {
    @Override
    public void process(DefaultGame game) {
        game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
        game.getGameState().sendMessage("DEBUG: Beginning BetweenTurnsProcess");
        PlayOrder playOrder = game.getGameState().getPlayerOrder().getClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);
        playOrder.getNextPlayer();

        String nextPlayer = playOrder.getNextPlayer();
        game.getGameState().startPlayerTurn(nextPlayer);
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesStartOfTurnGameProcess();
    }
}
