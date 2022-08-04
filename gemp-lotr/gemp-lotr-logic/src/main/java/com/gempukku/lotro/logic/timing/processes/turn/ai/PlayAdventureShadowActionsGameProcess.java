package com.gempukku.lotro.logic.timing.processes.turn.ai;

import com.gempukku.lotro.game.adventure.ShadowAI;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class PlayAdventureShadowActionsGameProcess implements GameProcess {
    private final ShadowAI _shadowAI;
    private final GameProcess _followingGameProcess;

    private GameProcess _nextProcess;

    public PlayAdventureShadowActionsGameProcess(ShadowAI shadowAI, GameProcess followingGameProcess) {
        _shadowAI = shadowAI;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
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
