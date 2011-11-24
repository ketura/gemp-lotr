package com.gempukku.lotro.logic.timing.processes.turn.assign;

import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayOrder;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ShadowPlayersAssignTheirMinionsGameProcess implements GameProcess {
    private GameProcess _followingProcess;
    private PlayOrder _shadowOrder;
    private String _firstShadowPlayer;

    public ShadowPlayersAssignTheirMinionsGameProcess(GameProcess followingProcess) {
        _followingProcess = followingProcess;
    }

    @Override
    public void process(LotroGame game) {
        GameState gameState = game.getGameState();
        _shadowOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        _shadowOrder.getNextPlayer();
        _firstShadowPlayer = _shadowOrder.getNextPlayer();

    }

    @Override
    public GameProcess getNextProcess() {
        return new ShadowPlayerAssignsHisMinionsGameProcess(_shadowOrder, _firstShadowPlayer, _followingProcess);
    }
}
