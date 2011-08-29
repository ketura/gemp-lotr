package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.actions.PlayerReconcilesAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class PlayerReconcilesGameProcess implements GameProcess {
    private LotroGame _game;
    private String _playerId;
    private GameProcess _followingGameProcess;

    public PlayerReconcilesGameProcess(LotroGame game, String playerId, GameProcess followingGameProcess) {
        _game = game;
        _playerId = playerId;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        _game.getActionsEnvironment().addActionToStack(new PlayerReconcilesAction(_game, _playerId));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
