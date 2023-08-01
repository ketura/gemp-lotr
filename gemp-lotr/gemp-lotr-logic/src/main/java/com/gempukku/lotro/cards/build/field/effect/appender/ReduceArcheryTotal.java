package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.effects.Effect;
import com.gempukku.lotro.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.modifiers.lotronly.ArcheryTotalModifier;
import com.gempukku.lotro.rules.RuleUtils;
import org.json.simple.JSONObject;

public class ReduceArcheryTotal implements EffectAppenderProducer {
    @Override
    public EffectAppender createEffectAppender(JSONObject effectObject, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "side", "memorize");

        final Side side = FieldUtils.getEnum(Side.class, effectObject.get("side"), "side");
        final String memorize = FieldUtils.getString(effectObject.get("memorize"), "memorize", "_temp");

        MultiEffectAppender result = new MultiEffectAppender();

        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        int archeryTotal = RuleUtils.calculateArcheryTotal(actionContext.getGame(), side);
                        return new PlayoutDecisionEffect(
                                actionContext.getPerformingPlayer(),
                                new IntegerAwaitingDecision(1, "Choose number to reduce archery by", 0, archeryTotal, archeryTotal) {
                                    @Override
                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                        final int validatedResult = getValidatedResult(result);
                                        actionContext.setValueToMemory(memorize, String.valueOf(validatedResult));
                                    }
                                });
                    }
                });
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(boolean cost, CostToEffectAction action, DefaultActionContext actionContext) {
                        int modifier = Integer.parseInt(actionContext.getValueFromMemory(memorize));
                        return new AddUntilEndOfPhaseModifierEffect(
                                new ArcheryTotalModifier(actionContext.getSource(), side, -modifier));
                    }
                }
        );

        return result;
    }
}
