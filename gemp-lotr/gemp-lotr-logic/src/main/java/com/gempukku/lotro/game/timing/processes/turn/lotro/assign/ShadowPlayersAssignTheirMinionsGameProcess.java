package com.gempukku.lotro.game.timing.processes.turn.lotro.assign;

import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.timing.PlayOrder;
import com.gempukku.lotro.game.timing.processes.GameProcess;

import java.util.Set;

public class ShadowPlayersAssignTheirMinionsGameProcess implements GameProcess {
    private final GameProcess _followingProcess;
    private final Set<PhysicalCard> _leftoverMinions;
    private PlayOrder _shadowOrder;
    private String _firstShadowPlayer;

    public ShadowPlayersAssignTheirMinionsGameProcess(GameProcess followingProcess, Set<PhysicalCard> leftoverMinions) {
        _followingProcess = followingProcess;
        _leftoverMinions = leftoverMinions;
    }

    @Override
    public void process(DefaultGame game) {
        GameState gameState = game.getGameState();
        _shadowOrder = gameState.getPlayerOrder().getCounterClockwisePlayOrder(gameState.getCurrentPlayerId(), false);
        _shadowOrder.getNextPlayer();
        _firstShadowPlayer = _shadowOrder.getNextPlayer();

    }

    @Override
    public GameProcess getNextProcess() {
        return new ShadowPlayerAssignsHisMinionsGameProcess(_shadowOrder, _firstShadowPlayer, _leftoverMinions, _followingProcess);
    }
}
