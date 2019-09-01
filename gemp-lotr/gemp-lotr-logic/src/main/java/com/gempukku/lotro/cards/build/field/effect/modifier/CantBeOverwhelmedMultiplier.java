package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import org.json.simple.JSONObject;

public class CantBeOverwhelmedMultiplier implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "multiplier");

        final String filter = FieldUtils.getString(value.get("filter"), "filter");
        final int multiplier = FieldUtils.getInteger(value.get("multiplier"), "multiplier", 3);

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

        blueprint.appendInPlayModifier(
                new ModifierSource() {
                    @Override
                    public Modifier getModifier(ActionContext actionContext) {
                        final Filterable filterable = filterableSource.getFilterable(actionContext);
                        return new OverwhelmedByMultiplierModifier(actionContext.getSource(), filterable, multiplier);
                    }
                }
        );
    }
}
