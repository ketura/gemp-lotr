package com.gempukku.lotro.processes.turn.tribbles;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.effects.TriggeringResultEffect;
import com.gempukku.lotro.effects.UnrespondableEffect;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.effects.results.StartOfTurnResult;

public class TribblesStartOfTurnGameProcess implements GameProcess {
    @Override
    public void process(DefaultGame game) {
//        game.getGameState().sendMessage("DEBUG: Beginning TribblesStartOfTurnGameProcess");
        game.getGameState().startAffectingCardsForCurrentPlayer(game);

        SystemQueueAction action = new SystemQueueAction();

        action.appendEffect(new UnrespondableEffect() {
            @Override
            protected void doPlayEffect(DefaultGame game) {
                var state = game.getGameState();
                state.sendMessage("\n\n========\n\nStart of " + state.getCurrentPlayerId() + "'s turn.");
            }
        });

        action.appendEffect(
                new TriggeringResultEffect(new StartOfTurnResult(), "Start of turn"));

        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesTurnProcess();
    }
}
