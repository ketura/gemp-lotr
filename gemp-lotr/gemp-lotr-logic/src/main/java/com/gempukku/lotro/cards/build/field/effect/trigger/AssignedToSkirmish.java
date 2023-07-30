package com.gempukku.lotro.cards.build.field.effect.trigger;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.TriggerConditions;
import com.gempukku.lotro.effects.results.AssignAgainstResult;
import org.json.simple.JSONObject;

public class AssignedToSkirmish implements TriggerCheckerProducer {
    @Override
    public TriggerChecker getTriggerChecker(JSONObject value, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "side", "memorize");

        final String filter = FieldUtils.getString(value.get("filter"), "filter", "any");
        final String memorize = FieldUtils.getString(value.get("memorize"), "memorize");
        final Side side = FieldUtils.getEnum(Side.class, value.get("side"), "side");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return new TriggerChecker() {
            @Override
            public boolean isBefore() {
                return false;
            }

            @Override
            public boolean accepts(ActionContext actionContext) {
                final Filterable assignedFilterable = filterableSource.getFilterable(actionContext);
                final boolean result = TriggerConditions.assignedToSkirmish(actionContext.getGame(), actionContext.getEffectResult(), side, assignedFilterable);
                if (result && memorize != null) {
                    AssignAgainstResult assignmentResult = (AssignAgainstResult) actionContext.getEffectResult();
                    actionContext.setCardMemory(memorize, assignmentResult.getAssignedCard());
                }
                return result;
            }
        };
    }
}
