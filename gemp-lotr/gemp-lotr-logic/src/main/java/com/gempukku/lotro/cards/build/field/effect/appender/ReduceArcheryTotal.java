package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.actions.lotronly.CostToEffectAction;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.game.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.game.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.game.modifiers.lotronly.ArcheryTotalModifier;
import com.gempukku.lotro.game.effects.Effect;
import com.gempukku.lotro.game.rules.RuleUtils;
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
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
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
                    protected Effect createEffect(boolean cost, CostToEffectAction action, ActionContext actionContext) {
                        int modifier = Integer.parseInt(actionContext.getValueFromMemory(memorize));
                        return new AddUntilEndOfPhaseModifierEffect(
                                new ArcheryTotalModifier(actionContext.getSource(), side, -modifier));
                    }
                }
        );

        return result;
    }
}
