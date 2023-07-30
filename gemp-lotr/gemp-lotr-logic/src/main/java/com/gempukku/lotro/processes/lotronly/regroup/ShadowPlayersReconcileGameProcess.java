package com.gempukku.lotro.processes.lotronly.regroup;

import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.gamestate.GameState;
import com.gempukku.lotro.game.PlayOrder;
import com.gempukku.lotro.processes.GameProcess;

public class ShadowPlayersReconcileGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    public ShadowPlayersReconcileGameProcess(GameProcess followingGameProcess) {
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process(DefaultGame game) {
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
