package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;

public class ManeuverGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(LotroGame game) {
        if (!Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.MINION)
                || game.getModifiersQuerying().shouldSkipPhase(game.getGameState(), Phase.MANEUVER, null))
            _followingGameProcess = new ArcheryGameProcess();
        else
            _followingGameProcess = new StartOfPhaseGameProcess(Phase.MANEUVER,
                    new PlayersPlayPhaseActionsInOrderGameProcess(game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(game.getGameState().getCurrentPlayerId(), true), 0,
                            new EndOfPhaseGameProcess(Phase.MANEUVER,
                                    new ArcheryGameProcess())));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
