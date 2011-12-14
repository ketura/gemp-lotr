package com.gempukku.lotro.logic.timing.processes.turn.skirmish;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.AssignmentGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.RegroupGameProcess;
import com.gempukku.lotro.logic.timing.results.AfterAllSkirmishesResult;

public class EndSkirmishesGameProcess implements GameProcess {
    private AfterAllSkirmishesResult _afterAllSkirmishesResult = new AfterAllSkirmishesResult();

    @Override
    public void process(LotroGame game) {
        SystemQueueAction action = new SystemQueueAction() {
            @Override
            public String getText(LotroGame game) {
                return "After all skirmishes";
            }
        };
        action.appendEffect(
                new TriggeringResultEffect(null, _afterAllSkirmishesResult, "After all skirmishes"));
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        if (_afterAllSkirmishesResult.isCreateAnExtraAssignmentAndSkirmishPhases()) {
            return new GameProcess() {
                @Override
                public void process(LotroGame game) {
                    game.getGameState().setExtraSkirmishes(true);
                }

                @Override
                public GameProcess getNextProcess() {
                    return new AssignmentGameProcess();
                }
            };
        } else {
            return new RegroupGameProcess();
        }
    }
}
