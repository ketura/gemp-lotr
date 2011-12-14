package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPlayersReconcileGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    public ShadowPlayersReconcileGameProcess(GameProcess followingGameProcess) {
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(LotroGame game) {
        GameState gameState = game.getGameState();
        PlayOrder reverseShadowOrder = gameState.getPlayerOrder().getClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        reverseShadowOrder.getNextPlayer();

        GameProcess following = _followingGameProcess;
        String nextShadowPlayer;
        while ((nextShadowPlayer = reverseShadowOrder.getNextPlayer()) != null)
            following = new PlayerReconcilesGameProcess(nextShadowPlayer, following);

        _followingGameProcess = following;
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
