package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.regroup.FellowshipPlayerChoosesToMoveOrStayGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.regroup.ShadowPlayersReconcileGameProcess;

public class RegroupGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(LotroGame game) {
        _followingGameProcess = new StartOfPhaseGameProcess(Phase.REGROUP,
                new PlayersPlayPhaseActionsInOrderGameProcess(game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true), 0,
                                new ShadowPlayersReconcileGameProcess(
                                        new FellowshipPlayerChoosesToMoveOrStayGameProcess())));

    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
