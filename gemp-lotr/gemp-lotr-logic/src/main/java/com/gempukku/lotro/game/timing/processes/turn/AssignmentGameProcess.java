package com.gempukku.lotro.game.timing.processes.turn;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.turn.assign.FreePeoplePlayerAssignsMinionsGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.skirmish.PlayoutSkirmishesGameProcess;

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
