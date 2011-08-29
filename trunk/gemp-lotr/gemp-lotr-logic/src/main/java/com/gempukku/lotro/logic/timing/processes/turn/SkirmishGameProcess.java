package com.gempukku.lotro.logic.timing.processes.turn;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.EndOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.PlayersPlayPhaseActionsInOrderGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.general.StartOfPhaseGameProcess;
import com.gempukku.lotro.logic.timing.processes.turn.skirmish.ResolveSkirmishGameProcess;

public class SkirmishGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    public SkirmishGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {

    }

    @Override
    public GameProcess getNextProcess() {
        return new StartOfPhaseGameProcess(_game, Phase.SKIRMISH,
                new PlayersPlayPhaseActionsInOrderGameProcess(_game, _game.getGameState().getPlayerOrder().getCounterClockwisePlayOrder(_game.getGameState().getCurrentPlayerId(), true), 0,
                        new ResolveSkirmishGameProcess(_game,
                                new EndOfPhaseGameProcess(_game, Phase.SKIRMISH, _followingGameProcess))));
    }
}
