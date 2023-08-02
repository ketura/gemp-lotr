package com.gempukku.lotro.processes.lotronly.regroup;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.processes.GameProcess;

public class DiscardAllMinionsGameProcess implements GameProcess {
    private final GameProcess _followingGameProcess;

    public DiscardAllMinionsGameProcess(GameProcess followingGameProcess) {
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
        SystemQueueAction action = new SystemQueueAction();
        action.appendEffect(
                new DiscardCardsFromPlayEffect(null, null, CardType.MINION));
        game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
