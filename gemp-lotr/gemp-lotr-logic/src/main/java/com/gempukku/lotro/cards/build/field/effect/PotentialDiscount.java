package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import org.json.simple.JSONObject;

public class PotentialDiscount implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "amount", "discount");

        final ValueSource amountSource = ValueResolver.resolveEvaluator(value.get("amount"), 0, environment);
        final JSONObject discount = (JSONObject) value.get("discount");

        blueprint.appendDiscountSource(
                new DiscountSource() {
                    @Override
                    public int getPotentialDiscount(ActionContext actionContext) {
                        return amountSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), actionContext.getSource());
                    }

                    @Override
                    public DiscountEffect getDiscountEffect(ActionContext actionContext) {
                        return null;
                    }
                });
    }
}
