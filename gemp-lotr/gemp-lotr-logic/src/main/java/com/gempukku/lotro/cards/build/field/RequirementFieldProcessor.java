package com.gempukku.lotro.cards.build.field;

import com.gempukku.lotro.cards.build.*;
import org.json.simple.JSONObject;

public class RequirementFieldProcessor implements FieldProcessor {
    @Override
    public void processField(String key, Object value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final JSONObject[] requirementsArray = FieldUtils.getObjectArray(value, key);
        for (JSONObject requirement : requirementsArray) {
            final Requirement conditionRequirement = environment.getRequirementFactory().getRequirement(requirement, environment);
            blueprint.appendPlayRequirement(conditionRequirement);
        }
    }
}
