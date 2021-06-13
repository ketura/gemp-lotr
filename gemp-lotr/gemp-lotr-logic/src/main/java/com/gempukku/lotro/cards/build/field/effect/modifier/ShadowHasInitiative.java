package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.logic.modifiers.HasInitiativeModifier;
import org.json.simple.JSONObject;

public class ShadowHasInitiative implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "condition");
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return (actionContext) -> new HasInitiativeModifier(actionContext.getSource(), new RequirementCondition(requirements, actionContext), Side.SHADOW);
    }
}
