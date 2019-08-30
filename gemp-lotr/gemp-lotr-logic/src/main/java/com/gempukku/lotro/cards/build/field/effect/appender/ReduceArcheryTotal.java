package com.gempukku.lotro.cards.build.field.effect.appender;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.EffectAppender;
import com.gempukku.lotro.cards.build.field.effect.EffectAppenderProducer;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.RuleUtils;
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
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        int archeryTotal = RuleUtils.calculateArcheryTotal(game, side);
                        return new PlayoutDecisionEffect(
                                self.getOwner(),
                                new IntegerAwaitingDecision(1, "Choose number to reduce archery by", 0, archeryTotal, archeryTotal) {
                                    @Override
                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                        final int validatedResult = getValidatedResult(result);
                                        action.setValueToMemory(memorize, String.valueOf(validatedResult));
                                    }
                                });
                    }
                });
        result.addEffectAppender(
                new DelayedAppender() {
                    @Override
                    protected Effect createEffect(CostToEffectAction action, String playerId, LotroGame game, PhysicalCard self, EffectResult effectResult, Effect effect) {
                        int modifier = Integer.parseInt(action.getValueFromMemory(memorize));
                        return new AddUntilEndOfPhaseModifierEffect(
                                new ArcheryTotalModifier(self, side, -modifier));
                    }
                }
        );

        return result;
    }
}
