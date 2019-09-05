package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.appender.resolver.ValueResolver;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscountEffect;
import com.gempukku.lotro.logic.effects.discount.DiscardCardFromHandDiscountEffect;
import org.json.simple.JSONObject;

public class PotentialDiscount implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "amount", "discount");

        final ValueSource amountSource = ValueResolver.resolveEvaluator(value.get("amount"), 0, environment);
        final JSONObject discount = (JSONObject) value.get("discount");

        final String discountType = FieldUtils.getString(discount.get("type"), "type");
        if (discountType.equals("discardFromHand")) {
            FieldUtils.validateAllowedFields(discount, "filter");

            final String filter = FieldUtils.getString(discount.get("filter"), "filter", "any");
            final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

            blueprint.appendDiscountSource(
                    new DiscountSource() {
                        @Override
                        public int getPotentialDiscount(ActionContext actionContext) {
                            return amountSource.getEvaluator(actionContext).evaluateExpression(actionContext.getGame(), actionContext.getSource());
                        }

                        @Override
                        public DiscountEffect getDiscountEffect(CostToEffectAction action, ActionContext actionContext) {
                            final Filterable filterable = filterableSource.getFilterable(actionContext);
                            return new DiscardCardFromHandDiscountEffect(action, actionContext.getPerformingPlayer(), filterable);
                        }
                    });
        } else {
            throw new InvalidCardDefinitionException("Unknown type of discount: " + discountType);
        }
    }
}
