package com.gempukku.lotro.game.timing.processes.turn.lotro.regroup;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.PlayerReconcilesAction;
import com.gempukku.lotro.game.timing.processes.GameProcess;

public class PlayerReconcilesGameProcess implements GameProcess {
    private final String _playerId;
    private final GameProcess _followingGameProcess;

    public PlayerReconcilesGameProcess(String playerId, GameProcess followingGameProcess) {
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
        game.getActionsEnvironment().addActionToStack(new PlayerReconcilesAction(game, _playerId));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
