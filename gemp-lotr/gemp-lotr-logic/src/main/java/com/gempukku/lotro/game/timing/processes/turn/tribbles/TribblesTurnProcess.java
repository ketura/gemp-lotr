package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.turn.StartOfPhaseGameProcess;

public class TribblesTurnProcess implements GameProcess {
    private GameProcess _followingGameProcess;
    @Override
    public void process(DefaultGame game) {
        game.getGameState().sendMessage("DEBUG: Beginning TribblesTurnProcess");
        _followingGameProcess = new StartOfPhaseGameProcess(
                Phase.FELLOWSHIP,
                new TribblesPlayerPlaysOrDraws(
                        game.getGameState().getCurrentPlayerId(),
                        new TribblesEndOfTurnGameProcess()
                )
        );
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
