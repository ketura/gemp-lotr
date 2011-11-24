package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.move.MovementGameProcess;

public class FellowshipGameProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(LotroGame game) {
        if (game.getModifiersQuerying().shouldSkipPhase(game.getGameState(), Phase.FELLOWSHIP, game.getGameState().getCurrentPlayerId()))
            _followingGameProcess = new ShadowPhasesGameProcess();
        else
            _followingGameProcess = new StartOfPhaseGameProcess(Phase.FELLOWSHIP,
                    new PlayerPlaysPhaseActionsUntilPassesGameProcess(game.getGameState().getCurrentPlayerId(),
                            new MovementGameProcess(
                                    new EndOfPhaseGameProcess(Phase.FELLOWSHIP,
                                            new ShadowPhasesGameProcess()))));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
