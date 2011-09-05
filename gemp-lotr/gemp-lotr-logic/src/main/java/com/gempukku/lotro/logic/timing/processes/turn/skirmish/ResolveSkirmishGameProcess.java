package com.gempukku.lotro.logic.timing.processes.turn.skirmish;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.actions.ResolveSkirmishAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ResolveSkirmishGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    public ResolveSkirmishGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        Skirmish skirmish = _game.getGameState().getSkirmish();
        if (!skirmish.isCancelled())
            _game.getActionsEnvironment().addActionToStack(new ResolveSkirmishAction(_game, skirmish));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
