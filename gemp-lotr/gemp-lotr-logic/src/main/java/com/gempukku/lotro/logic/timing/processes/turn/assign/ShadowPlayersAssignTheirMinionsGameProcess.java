package com.gempukku.lotro.logic.timing.processes.turn.assign;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPlayersAssignTheirMinionsGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingProcess;

    public ShadowPlayersAssignTheirMinionsGameProcess(LotroGame game, GameProcess followingProcess) {
        _game = game;
        _followingProcess = followingProcess;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        GameState gameState = _game.getGameState();
        PlayOrder shadowOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        shadowOrder.getNextPlayer();
        String firstShadowPlayer = shadowOrder.getNextPlayer();
        return new ShadowPlayerAssignsHisMinionsGameProcess(_game, shadowOrder, firstShadowPlayer, _followingProcess);
    }
}
