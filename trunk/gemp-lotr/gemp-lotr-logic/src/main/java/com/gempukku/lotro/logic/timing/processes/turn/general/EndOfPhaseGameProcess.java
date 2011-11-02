package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.EndOfPhaseResult;

import java.util.Collection;

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
        SystemQueueAction action = new SystemQueueAction() {
            @Override
            public String getText(LotroGame game) {
                return "End of " + _phase + " phase";
            }
        };
        action.appendEffect(
                new TriggeringResultEffect(Effect.Type.END_OF_PHASE, new EndOfPhaseResult(_phase), "End of " + _phase + " phase"));
        action.appendEffect(
                new AbstractSuccessfulEffect() {
                    @Override
                    public String getText(LotroGame game) {
                        return null;
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }

                    @Override
                    public Collection<? extends EffectResult> playEffect(LotroGame game) {
                        ((ModifiersLogic) game.getModifiersEnvironment()).removeEndOfPhase(_phase);
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).removeEndOfPhaseActionProxies(_phase);
                        return null;
                    }
                });
        _game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
