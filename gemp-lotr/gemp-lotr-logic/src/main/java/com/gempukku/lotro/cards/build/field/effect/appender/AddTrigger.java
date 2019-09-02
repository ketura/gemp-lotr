package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.TimeResolver;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerChecker;
import com.gempukku.lotro.game.ActionProxy;
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
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                ActionProxy actionProxy = createActionProxy(action, actionContext, optional, trigger, requirements, costs, effects);

                if (until.isStart()) {
                    return new AddUntilStartOfPhaseActionProxyEffect(actionProxy, until.getPhase());
                } else {
                    return new AddUntilEndOfPhaseActionProxyEffect(actionProxy, until.getPhase());
                }
            }
        };
    }

    private ActionProxy createActionProxy(CostToEffectAction action, ActionContext actionContext, boolean optional, TriggerChecker trigger, Requirement[] requirements, EffectAppender[] costs, EffectAppender[] effects) {
        return new ActionProxy() {
            private boolean checkRequirements(ActionContext actionContext) {
                for (Requirement requirement : requirements) {
                    if (!requirement.accepts(actionContext))
                        return true;
                }

                for (EffectAppender cost : costs) {
                    if (!cost.isPlayableInFull(actionContext))
                        return true;
                }
                return false;
            }

            private void customizeTriggerAction(AbstractCostToEffectAction action, ActionContext actionContext) {
                action.setVirtualCardAction(true);
                for (EffectAppender cost : costs)
                    cost.appendEffect(true, action, actionContext);
                for (EffectAppender effectAppender : effects)
                    effectAppender.appendEffect(false, action, actionContext);
            }

            @Override
            public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame lotroGame, Effect effect) {
                if (trigger.isBefore() && !optional && trigger.accepts(actionContext)) {
                    if (checkRequirements(actionContext))
                        return null;

                    DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                            lotroGame, actionContext.getSource(), null, effect);
                    RequiredTriggerAction result = new RequiredTriggerAction(actionContext.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }

            @Override
            public List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame lotroGame, Effect effect) {
                if (trigger.isBefore() && optional && trigger.accepts(actionContext)) {
                    if (checkRequirements(actionContext))
                        return null;

                    DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                            lotroGame, actionContext.getSource(), null, effect);
                    OptionalTriggerAction result = new OptionalTriggerAction(actionContext.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }

            @Override
            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                if (!trigger.isBefore() && !optional && trigger.accepts(actionContext)) {
                    if (checkRequirements(actionContext))
                        return null;

                    DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                            lotroGame, actionContext.getSource(), effectResult, null);
                    RequiredTriggerAction result = new RequiredTriggerAction(actionContext.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }

            @Override
            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame lotroGame, EffectResult effectResult) {
                if (!trigger.isBefore() && optional && trigger.accepts(actionContext)) {
                    if (checkRequirements(actionContext))
                        return null;

                    DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                            lotroGame, actionContext.getSource(), effectResult, null);
                    OptionalTriggerAction result = new OptionalTriggerAction(actionContext.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }
        };
    }
}
