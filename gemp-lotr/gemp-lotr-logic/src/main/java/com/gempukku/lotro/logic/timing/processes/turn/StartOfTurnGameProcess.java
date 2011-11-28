package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.StartOfTurnResult;

public class StartOfTurnGameProcess implements GameProcess {
    @Override
    public void process(LotroGame game) {
        PlayOrder playOrder = game.getGameState().getPlayerOrder().getClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), false);
        playOrder.getNextPlayer();

        String nextPlayer = playOrder.getNextPlayer();
        game.getGameState().setCurrentPhase(Phase.BETWEEN_TURNS);
        game.getGameState().startPlayerTurn(nextPlayer);
        game.getGameState().startAffectingCardsForCurrentPlayer(game);

        SystemQueueAction action = new SystemQueueAction();

        action.appendEffect(
                new TriggeringResultEffect(new StartOfTurnResult(), "Start of turn"));

        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipGameProcess();
    }
}
