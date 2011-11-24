package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.EndOfTurnGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;

public class DiscardAllMinionsGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(LotroGame game) {
        SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new DiscardCardsFromPlayEffect(null, CardType.MINION));
        game.getActionsEnvironment().addActionToStack(action);
        _followingGameProcess = new EndOfPhaseGameProcess(Phase.REGROUP,
                new EndOfTurnGameProcess());
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
