package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.turn.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.StartOfPhaseGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.EndOfTurnGameProcess;

public class TribblesTurnProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(DefaultGame game) {
        game.getGameState().sendMessage("DEBUG: Beginning TribblesTurnProcess");
        _followingGameProcess = new StartOfPhaseGameProcess(
                Phase.FELLOWSHIP,
                new PlayerPlaysPhaseActionsUntilPassesGameProcess(
                        game.getGameState().getCurrentPlayerId(),
                        new EndOfTurnGameProcess()
                )
        );
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
