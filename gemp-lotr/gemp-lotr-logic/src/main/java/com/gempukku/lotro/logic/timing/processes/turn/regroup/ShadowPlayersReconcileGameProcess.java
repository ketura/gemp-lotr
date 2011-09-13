package com.gempukku.lotro.logic.timing.processes.turn.regroup;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPlayersReconcileGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    public ShadowPlayersReconcileGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        GameState gameState = _game.getGameState();
        gameState.setFierceSkirmishes(false);
        PlayOrder reverseShadowOrder = gameState.getPlayerOrder().getClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        reverseShadowOrder.getNextPlayer();

        GameProcess following = _followingGameProcess;
        String nextShadowPlayer;
        while ((nextShadowPlayer = reverseShadowOrder.getNextPlayer()) != null)
            following = new PlayerReconcilesGameProcess(_game, nextShadowPlayer, following);

        return following;
    }
}
