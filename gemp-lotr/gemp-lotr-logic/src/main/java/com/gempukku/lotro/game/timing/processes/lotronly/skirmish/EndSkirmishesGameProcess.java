package com.gempukku.lotro.game.timing.processes.lotronly.skirmish;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.AssignmentGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.RegroupGameProcess;
import com.gempukku.lotro.game.timing.results.AfterAllSkirmishesResult;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.effects.TriggeringResultEffect;

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
