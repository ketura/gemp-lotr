package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.TimeResolver;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerChecker;
import com.gempukku.lotro.game.ActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.AbstractCostToEffectAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;

public class AddTrigger implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "trigger", "until", "optional", "condition", "cost", "effect");

        final TimeResolver.Time until = TimeResolver.resolveTime(effectObject.get("until"), "end(current)");
        final TriggerChecker trigger = environment.getTriggerCheckerFactory().getTriggerChecker((JSONObject) effectObject.get("trigger"), environment);
        final boolean optional = FieldUtils.getBoolean(effectObject.get("optional"), "optional", false);

        final JSONObject[] requirementArray = FieldUtils.getObjectArray(effectObject.get("condition"), "condition");
        final JSONObject[] costArray = FieldUtils.getObjectArray(effectObject.get("cost"), "cost");
        final JSONObject[] effectArray = FieldUtils.getObjectArray(effectObject.get("effect"), "effect");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(requirementArray, environment);
        final EffectAppender[] costs = environment.getEffectAppenderFactory().getEffectAppenders(costArray, environment);
        final EffectAppender[] effects = environment.getEffectAppenderFactory().getEffectAppenders(effectArray, environment);


        return new DelayedAppender() {
            @Override
            protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                ActionProxy actionProxy = createActionProxy(action, self, optional, trigger, requirements, costs, effects);

                if (until.isStart()) {
                    return new AddUntilStartOfPhaseActionProxyEffect(actionProxy, until.getPhase());
                } else {
                    return new AddUntilEndOfPhaseActionProxyEffect(actionProxy, until.getPhase());
                }
            }
        };
    }

    private ActionProxy createActionProxy(CostToEffectAction action, PhysicalCard self, boolean optional, TriggerChecker trigger, Requirement[] requirements, EffectAppender[] costs, EffectAppender[] effects) {
        return new ActionProxy() {
            private boolean checkRequirements(String playerId, CostToEffectAction action, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                for (Requirement requirement : requirements) {
                    if (!requirement.accepts(action, playerId, game, self, effectResult, effect))
                        return true;
                }

                for (EffectAppender cost : costs) {
                    if (!cost.isPlayableInFull(action, playerId, game, self, effectResult, effect))
                        return true;
                }
                return false;
            }

            private void customizeTriggerAction(AbstractCostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                action.setVirtualCardAction(true);
                for (EffectAppender cost : costs)
                    cost.appendCost(action, playerId, game, self, effectResult, effect);
                for (EffectAppender effectAppender : effects)
                    effectAppender.appendEffect(action, playerId, game, self, effectResult, effect);
            }

            @Override
            public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame lotroGame, Effect effect) {
                if (trigger.isBefore() && !optional && trigger.accepts(action, null, lotroGame, self, null, effect)) {
                    if (checkRequirements(null, null, lotroGame, self, null, effect))
                        return null;

                    RequiredTriggerAction action = new RequiredTriggerAction(self);
                    customizeTriggerAction(action, null, lotroGame, self, null, effect);

                    return Collections.singletonList(action);
                }
                return null;
            }

            @Override
            public List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame lotroGame, Effect effect) {
                if (trigger.isBefore() && optional && trigger.accepts(action, playerId, lotroGame, self, null, effect)) {
                    if (checkRequirements(playerId, null, lotroGame, self, null, effect))
                        return null;

                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    customizeTriggerAction(action, playerId, lotroGame, self, null, effect);

                    return Collections.singletonList(action);
                }
                return null;
            }

            @Override
            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                if (!trigger.isBefore() && !optional && trigger.accepts(action, null, lotroGame, self, effectResult, null)) {
                    if (checkRequirements(null, null, lotroGame, self, effectResult, null))
                        return null;

                    RequiredTriggerAction action = new RequiredTriggerAction(self);
                    customizeTriggerAction(action, null, lotroGame, self, effectResult, null);

                    return Collections.singletonList(action);
                }
                return null;
            }

            @Override
            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame lotroGame, EffectResult effectResult) {
                if (!trigger.isBefore() && optional && trigger.accepts(action, playerId, lotroGame, self, effectResult, null)) {
                    if (checkRequirements(playerId, null, lotroGame, self, effectResult, null))
                        return null;

                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                    customizeTriggerAction(action, playerId, lotroGame, self, effectResult, null);

                    return Collections.singletonList(action);
                }
                return null;
            }
        };
    }
}
