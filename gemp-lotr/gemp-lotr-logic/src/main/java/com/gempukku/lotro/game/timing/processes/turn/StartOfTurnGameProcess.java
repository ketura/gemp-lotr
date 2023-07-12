package com.gempukku.lotro.game.timing.processes.turn;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.effects.TriggeringResultEffect;
import com.gempukku.lotro.game.effects.UnrespondableEffect;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.FellowshipGameProcess;
import com.gempukku.lotro.game.timing.results.StartOfTurnResult;

public class StartOfTurnGameProcess implements GameProcess {
    @Override
    public void process(DefaultGame game) {
        game.getGameState().startAffectingCardsForCurrentPlayer(game);

        SystemQueueAction action = new SystemQueueAction();

        action.appendEffect(new UnrespondableEffect() {
            @Override
            protected void doPlayEffect(DefaultGame game) {
                var state = game.getGameState();
                state.sendMessage("\n\n========\n\nStart of turn.");
                state.sendMessage("Free Peoples player: " + state.getCurrentPlayerId());
            }
        });

        action.appendEffect(
                new TriggeringResultEffect(new StartOfTurnResult(), "Start of turn"));

        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipGameProcess();
    }
}
