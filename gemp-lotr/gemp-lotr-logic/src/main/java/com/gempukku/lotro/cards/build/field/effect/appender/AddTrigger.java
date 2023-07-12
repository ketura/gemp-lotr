package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.TimeResolver;
import com.gempukku.lotro.cards.build.field.effect.trigger.TriggerChecker;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.AbstractActionProxy;
import com.gempukku.lotro.game.actions.ActionProxy;
import com.gempukku.lotro.game.actions.lotronly.AbstractCostToEffectAction;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.actions.lotronly.OptionalTriggerAction;
import com.gempukku.lotro.game.actions.lotronly.RequiredTriggerAction;
import com.gempukku.lotro.game.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.game.effects.AddUntilEndOfTurnActionProxyEffect;
import com.gempukku.lotro.game.effects.AddUntilStartOfPhaseActionProxyEffect;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.effects.EffectResult;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;

public class AddTrigger implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "trigger", "until", "optional", "requires", "cost", "effect");

        final TimeResolver.Time until = TimeResolver.resolveTime(effectObject.get("until"), "end(current)");
        final TriggerChecker trigger = environment.getTriggerCheckerFactory().getTriggerChecker((JSONObject) effectObject.get("trigger"), environment);
        final boolean optional = FieldUtils.getBoolean(effectObject.get("optional"), "optional", false);

        final JSONObject[] requirementArray = FieldUtils.getObjectArray(effectObject.get("requires"), "requires");
        final JSONObject[] costArray = FieldUtils.getObjectArray(effectObject.get("cost"), "cost");
        final JSONObject[] effectArray = FieldUtils.getObjectArray(effectObject.get("effect"), "effect");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(requirementArray, environment);
        final EffectAppender[] costs = environment.getEffectAppenderFactory().getEffectAppenders(costArray, environment);
        final EffectAppender[] effects = environment.getEffectAppenderFactory().getEffectAppenders(effectArray, environment);

        return new DelayedAppender() {
            @Override
            protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                ActionProxy actionProxy = createActionProxy(action, actionContext, optional, trigger, requirements, costs, effects);

                if (until.isEndOfTurn()) {
                    return new AddUntilEndOfTurnActionProxyEffect(actionProxy);
                } else if (until.isStart()) {
                    return new AddUntilStartOfPhaseActionProxyEffect(actionProxy, until.getPhase());
                } else {
                    return new AddUntilEndOfPhaseActionProxyEffect(actionProxy, until.getPhase());
                }
            }
        };
    }

    private ActionProxy createActionProxy(CostToEffectAction action, ActionContext actionContext, boolean optional, TriggerChecker trigger, Requirement[] requirements, EffectAppender[] costs, EffectAppender[] effects) {
        return new AbstractActionProxy() {
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
            public List<? extends RequiredTriggerAction> getRequiredBeforeTriggers(DefaultGame lotroGame, Effect effect) {
                DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                        lotroGame, actionContext.getSource(), null, effect);
                if (trigger.isBefore() && !optional && trigger.accepts(delegate)) {
                    if (checkRequirements(delegate))
                        return null;

                    RequiredTriggerAction result = new RequiredTriggerAction(delegate.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }

            @Override
            public List<? extends OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, DefaultGame lotroGame, Effect effect) {
                DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                        lotroGame, actionContext.getSource(), null, effect);
                if (trigger.isBefore() && optional && trigger.accepts(delegate)) {
                    if (checkRequirements(delegate))
                        return null;

                    OptionalTriggerAction result = new OptionalTriggerAction(delegate.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }

            @Override
            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(DefaultGame lotroGame, EffectResult effectResult) {
                DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                        lotroGame, actionContext.getSource(), effectResult, null);
                if (!trigger.isBefore() && !optional && trigger.accepts(delegate)) {
                    if (checkRequirements(delegate))
                        return null;

                    RequiredTriggerAction result = new RequiredTriggerAction(delegate.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }

            @Override
            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, DefaultGame lotroGame, EffectResult effectResult) {
                DelegateActionContext delegate = new DelegateActionContext(actionContext, actionContext.getPerformingPlayer(),
                        lotroGame, actionContext.getSource(), effectResult, null);
                if (!trigger.isBefore() && optional && trigger.accepts(delegate)) {
                    if (checkRequirements(delegate))
                        return null;

                    OptionalTriggerAction result = new OptionalTriggerAction(delegate.getSource());
                    customizeTriggerAction(result, delegate);

                    return Collections.singletonList(result);
                }
                return null;
            }
        };
    }
}
