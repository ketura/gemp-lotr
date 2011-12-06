package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class BetweenTurnsProcess implements GameProcess {
    @Override
    public void process(LotroGame game) {
        game.getGameState().stopAffectingCardsForCurrentPlayer();
        game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);

        PlayOrder playOrder = game.getGameState().getPlayerOrder().getClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);
        playOrder.getNextPlayer();

        String nextPlayer = playOrder.getNextPlayer();
        game.getGameState().startPlayerTurn(nextPlayer);
        game.getGameState().startAffectingCardsForCurrentPlayer(game);

    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfTurnGameProcess();
    }
}
