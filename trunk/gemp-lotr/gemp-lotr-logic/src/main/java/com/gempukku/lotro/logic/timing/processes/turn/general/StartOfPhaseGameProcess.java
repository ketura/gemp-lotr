package com.gempukku.lotro.logic.timing.processes.turn.general;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.actions.SystemQueueAction;
import com.gempukku.lotro.logic.effects.TriggeringResultEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.processes.GameProcess;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

import java.util.Collection;

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
        SystemQueueAction action = new SystemQueueAction() {
            @Override
            public String getText(LotroGame game) {
                return "Start of " + _phase + " phase";
            }
        };
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
                        ((ModifiersLogic) game.getModifiersEnvironment()).removeStartOfPhase(_phase);
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).removeStartOfPhaseActionProxies(_phase);
                        return null;
                    }
                });
        action.appendEffect(
                new TriggeringResultEffect(null, new StartOfPhaseResult(_phase), "Start of " + _phase + " phase"));

        _game.getActionsEnvironment().addActionToStack(action);
    }

    @Override
    public GameProcess getNextProcess() {
        return _followingGameProcess;
    }
}
