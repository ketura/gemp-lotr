package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import org.json.simple.JSONObject;

public class KilledTriggerEffectProcessor implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "optional", "requires", "cost", "effect");

        final boolean optional = FieldUtils.getBoolean(value.get("optional"), "optional", false);

        DefaultActionSource triggerActionSource = new DefaultActionSource();
        EffectUtils.processRequirementsCostsAndEffects(value, environment, triggerActionSource);
        if (optional)
            blueprint.setKilledOptionalTriggerAction(triggerActionSource);
        else
            blueprint.setKilledRequiredTriggerAction(triggerActionSource);
    }
}
