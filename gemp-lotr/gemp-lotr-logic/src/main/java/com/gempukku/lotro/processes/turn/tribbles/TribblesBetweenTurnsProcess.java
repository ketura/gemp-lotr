package com.gempukku.lotro.processes.turn.tribbles;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.game.TribblesGame;
import com.gempukku.lotro.processes.DefaultGameProcess;
import com.gempukku.lotro.gamestate.tribbles.TribblesGameState;

public class TribblesBetweenTurnsProcess extends DefaultGameProcess<TribblesGame> {

    @Override
    public void process(TribblesGame game) {
        TribblesGameState gameState = game.getGameState();
        game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
        PlayOrder playOrder = gameState.getPlayerOrder().getStandardPlayOrder(gameState.getCurrentPlayerId(), false);

        playOrder.getNextPlayer(); // TODO: This call is necessary but not logical
        String currentPlayer = playOrder.getNextPlayer();

        while (game.getGameState().getPlayerDecked(currentPlayer)) {
            game.getGameState().sendMessage(currentPlayer + " is decked. Skipping their turn.");
            playOrder = gameState.getPlayerOrder().getStandardPlayOrder(currentPlayer, false);
            currentPlayer = playOrder.getNextPlayer();
        }

/*        String playOrderLabel;
        if (!game.getGameState().getPlayerOrder().getReversed()) {
            playOrderLabel = "clockwise";
        } else {
            playOrderLabel = "counterclockwise";
        }*/
//        game.getGameState().sendMessage("Play advances " + playOrderLabel + ".");
        game.getGameState().startPlayerTurn(currentPlayer);
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesStartOfTurnGameProcess();
    }
}
