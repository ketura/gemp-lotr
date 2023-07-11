package com.gempukku.lotro.game.timing.processes.turn.lotro.ai;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.adventure.ShadowAI;
import com.gempukku.lotro.game.actions.Action;
import com.gempukku.lotro.game.timing.processes.GameProcess;

public class PlayAdventureShadowActionsGameProcess implements GameProcess {
    private final ShadowAI _shadowAI;
    private final GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayAdventureShadowActionsGameProcess(ShadowAI shadowAI, GameProcess followingGameProcess) {
        _shadowAI = shadowAI;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
        Action action = _shadowAI.getNextShadowAction(game);
        if (action != null) {
            game.getActionsEnvironment().addActionToStack(action);
            _nextProcess = new PlayAdventureShadowActionsGameProcess(_shadowAI, _followingGameProcess);
        } else {
            _nextProcess = _followingGameProcess;
        }
    }

    @Override
    public GameProcess getNextProcess() {
        return _nextProcess;
    }
}
