package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import org.json.simple.JSONObject;

public class CantBeExerted implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "condition", "by");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");
        final String filter = FieldUtils.getString(value.get("filter"), "filter", "self");
        final String byFilter = FieldUtils.getString(value.get("by"), "by", "any");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
        final FilterableSource byFilterableSource = environment.getFilterFactory().generateFilter(byFilter);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                (actionContext) -> {
                    return new CantExertWithCardModifier(actionContext.getSource(),
                            filterableSource.getFilterable(actionContext),
                            new RequirementCondition(requirements, actionContext),
                            byFilterableSource.getFilterable(actionContext));
                });

    }
}
