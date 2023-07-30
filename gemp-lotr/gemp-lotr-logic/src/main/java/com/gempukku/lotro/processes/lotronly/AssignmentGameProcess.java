package com.gempukku.lotro.processes.lotronly;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.turn.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.processes.lotronly.assign.FreePeoplePlayerAssignsMinionsGameProcess;
import com.gempukku.lotro.processes.turn.EndOfPhaseGameProcess;
import com.gempukku.lotro.processes.turn.StartOfPhaseGameProcess;
import com.gempukku.lotro.processes.lotronly.skirmish.PlayoutSkirmishesGameProcess;

public class AssignmentGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(DefaultGame game) {
        if (Filters.countActive(game, CardType.MINION)==0
                || game.getModifiersQuerying().shouldSkipPhase(game, Phase.ASSIGNMENT, null))
            _followingGameProcess = new RegroupGameProcess();
        else
            _followingGameProcess = new StartOfPhaseGameProcess(Phase.ASSIGNMENT,
                    new PlayersPlayPhaseActionsInOrderGameProcess(game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true), 0,
                            new FreePeoplePlayerAssignsMinionsGameProcess(
                                    new EndOfPhaseGameProcess(Phase.ASSIGNMENT,
                                            new PlayoutSkirmishesGameProcess()))));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
