package com.gempukku.lotro.game.timing.processes.turn.tribbles;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.MovementGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.ShadowPhasesGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.EndOfPhaseGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.game.timing.processes.turn.StartOfPhaseGameProcess;

public class TribblesTurnProcess implements GameProcess {
    private GameProcess _followingGameProcess;

    @Override
    public void process(DefaultGame game) {
        game.getGameState().sendMessage("DEBUG: Beginning TribblesTurnProcess");
        if (game.getModifiersQuerying().shouldSkipPhase(game, Phase.FELLOWSHIP, game.getGameState().getCurrentPlayerId()))
            _followingGameProcess = new ShadowPhasesGameProcess();
        else
            _followingGameProcess = new StartOfPhaseGameProcess(Phase.FELLOWSHIP,
                    new PlayerPlaysPhaseActionsUntilPassesGameProcess(game.getGameState().getCurrentPlayerId(),
                            new MovementGameProcess(
                                    new EndOfPhaseGameProcess(Phase.FELLOWSHIP,
                                            game.getFormat().getAdventure().getAfterFellowshipPhaseGameProcess()))));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
