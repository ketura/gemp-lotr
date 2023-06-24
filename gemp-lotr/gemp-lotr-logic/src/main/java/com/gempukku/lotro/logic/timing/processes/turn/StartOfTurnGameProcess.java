package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.StartOfTurnResult;

public class StartOfTurnGameProcess implements GameProcess {
    @Override
    public void process(LotroGame game) {
        game.getGameState().startAffectingCardsForCurrentPlayer(game);

        SystemQueueAction action = new SystemQueueAction();

        action.appendEffect(
                new TriggeringResultEffect(new StartOfTurnResult(), "Start of turn"));

        action.appendEffect(new UnrespondableEffect() {
            @Override
            protected void doPlayEffect(LotroGame game) {
                var state = game.getGameState();
                state.sendMessage("Start of turn.");
                state.sendMessage("Free Peoples player: " + state.getCurrentPlayerId());
            }
        });

        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipGameProcess();
    }
}
