package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.EndOfPhaseResult;

public class EndOfPhaseGameProcess implements GameProcess {
    private LotroGame _game;
    private Phase _phase;
    private GameProcess _followingGameProcess;

    public EndOfPhaseGameProcess(LotroGame game, Phase phase, GameProcess followingGameProcess) {
        _game = game;
        _phase = phase;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        _game.getActionsEnvironment().addActionToStack(new SimpleEffectAction(new TriggeringResultEffect(Effect.Type.END_OF_PHASE, new EndOfPhaseResult(_phase), "End of " + _phase + " phase"), "End of " + _phase + " phase"));
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
