package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.turn.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.processes.turn.StartOfPhaseGameProcess;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.lotronly.regroup.FellowshipPlayerChoosesToMoveOrStayGameProcess;

public class RegroupGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(DefaultGame game) {
        _followingGameProcess = new StartOfPhaseGameProcess(Phase.REGROUP,
                new PlayersPlayPhaseActionsInOrderGameProcess(game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true), 0,
                        game.getFormat().getAdventure().getBeforeFellowshipChooseToMoveGameProcess(
                                        new FellowshipPlayerChoosesToMoveOrStayGameProcess())));

    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
