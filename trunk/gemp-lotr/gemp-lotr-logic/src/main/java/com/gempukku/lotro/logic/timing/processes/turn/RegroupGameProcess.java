package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.regroup.FellowshipPlayerChoosesToMoveOrStayGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.regroup.ShadowPlayersReconcileGameProcess;

public class RegroupGameProcess implements GameProcess {
    private LotroGame _game;

    public RegroupGameProcess(LotroGame game) {
        _game = game;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfPhaseGameProcess(_game, Phase.REGROUP,
                new PlayersPlayPhaseActionsInOrderGameProcess(_game, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0,
                        new ShadowPlayersReconcileGameProcess(_game,
                                new FellowshipPlayerChoosesToMoveOrStayGameProcess(_game))));
    }
}
