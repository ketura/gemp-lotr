package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

public class StartOfPhaseGameProcess implements GameProcess {
    private LotroGame _game;
    private Phase _phase;
    private GameProcess _followingGameProcess;

    public StartOfPhaseGameProcess(LotroGame game, Phase phase, GameProcess followingGameProcess) {
        _game = game;
        _phase = phase;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        _game.getGameState().setCurrentPhase(_phase);
        _game.getActionsEnvironment().addActionToStack(new SimpleEffectAction(new TriggeringResultEffect(new StartOfPhaseResult(_phase), "Start of " + _phase + " phase"), "Start of " + _phase + " phase"));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
