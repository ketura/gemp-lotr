package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayerPlaysPhaseActionsUntilPassesGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.move.MovementGameProcess;

public class FellowshipGameProcess implements GameProcess {
    private LotroGame _game;

    public FellowshipGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process(LotroGame game) {

    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfPhaseGameProcess(_game, Phase.FELLOWSHIP,
                new PlayerPlaysPhaseActionsUntilPassesGameProcess(_game, _game.getGameState().getCurrentPlayerId(),
                        new MovementGameProcess(
                                new EndOfPhaseGameProcess(_game, Phase.FELLOWSHIP,
                                        new ShadowPhasesGameProcess(_game)))));
    }
}
