package com.gempukku.lotro.processes.lotronly.skirmish;

import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.effects.TriggeringResultEffect;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.lotronly.AssignmentGameProcess;
import com.gempukku.lotro.processes.lotronly.RegroupGameProcess;
import com.gempukku.lotro.effects.results.AfterAllSkirmishesResult;

public class EndSkirmishesGameProcess implements GameProcess {
    private final AfterAllSkirmishesResult _afterAllSkirmishesResult = new AfterAllSkirmishesResult();

    @Override
    public void process(DefaultGame game) {
        SystemQueueAction action = new SystemQueueAction();
        action.setText("After all skirmishes");
        action.appendEffect(
                new TriggeringResultEffect(null, _afterAllSkirmishesResult, "After all skirmishes"));
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        if (_afterAllSkirmishesResult.isCreateAnExtraAssignmentAndSkirmishPhases()) {
            return new GameProcess() {
                @Override
                public void process(DefaultGame game) {
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
