package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.game.timing.processes.DefaultGameProcess;

public class TribblesBetweenTurnsProcess extends DefaultGameProcess<TribblesGame> {

    @Override
    public void process(TribblesGame game) {
        game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
        PlayOrder playOrder = game.getGameState().getPlayerOrder().getClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);

        playOrder.getNextPlayer();
        String currentPlayer = playOrder.getNextPlayer();

        while (game.getGameState().getPlayerDecked(currentPlayer)) {
            game.getGameState().sendMessage(currentPlayer + " is decked. Skipping their turn.");
            playOrder = game.getGameState().getPlayerOrder().getClockwisePlayOrder(currentPlayer, false);
            currentPlayer = playOrder.getNextPlayer();
        }

        game.getGameState().startPlayerTurn(currentPlayer);
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesStartOfTurnGameProcess();
    }
}
