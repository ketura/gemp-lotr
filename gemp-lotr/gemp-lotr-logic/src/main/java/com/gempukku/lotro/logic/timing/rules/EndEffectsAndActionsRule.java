package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.actions.SimpleEffectAction;

import java.util.Collections;
import java.util.List;

public class EndEffectsAndActionsRule {
    private DefaultActionsEnvironment _actionsEnvironment;
    private ModifiersLogic _modifiersLogic;

    public EndEffectsAndActionsRule(DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        _actionsEnvironment = actionsEnvironment;
        _modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        _actionsEnvironment.addAlwaysOnActionProxy(
                new AbstractActionProxy() {
                    @Override
                    public List<? extends Action> getRequiredBeforeTriggers(final LotroGame lotroGame, Effect effect) {
                        if (effect.getType() == EffectResult.Type.START_OF_PHASE)
                            return Collections.<Action>singletonList(new SimpleEffectAction(
                                    new UnrespondableEffect() {
                                        @Override
                                        public void doPlayEffect(LotroGame game) {
                                            Phase phase = game.getGameState().getCurrentPhase();
                                            _modifiersLogic.removeStartOfPhase(phase);
                                            _actionsEnvironment.removeStartOfPhaseActionProxies(phase);
                                        }
                                    }, "Remove effects"
                            ));
                        else if (effect.getType() == EffectResult.Type.END_OF_PHASE)
                            return Collections.<Action>singletonList(new SimpleEffectAction(
                                    new UnrespondableEffect() {
                                        @Override
                                        public void doPlayEffect(LotroGame game) {
                                            Phase phase = game.getGameState().getCurrentPhase();
                                            _modifiersLogic.removeEndOfPhase(phase);
                                            _actionsEnvironment.removeEndOfPhaseActionProxies(phase);
                                        }
                                    }, "Remove effects"
                            ));
                        else if (effect.getType() == EffectResult.Type.END_OF_TURN)
                            return Collections.<Action>singletonList(new SimpleEffectAction(
                                    new UnrespondableEffect() {
                                        @Override
                                        public void doPlayEffect(LotroGame game) {
                                            _modifiersLogic.removeEndOfTurn();
                                            _actionsEnvironment.removeEndOfTurnActionProxies();
                                        }
                                    }, "Remove effects"
                            ));
                        else
                            return null;
                    }
                }
        );
    }
}
