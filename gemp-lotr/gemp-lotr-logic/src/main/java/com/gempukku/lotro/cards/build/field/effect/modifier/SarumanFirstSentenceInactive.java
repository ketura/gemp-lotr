package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;
import org.json.simple.JSONObject;

public class SarumanFirstSentenceInactive implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object,"condition");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(ActionContext actionContext) {
                return new SpecialFlagModifier(actionContext.getSource(),
                        new RequirementCondition(requirements, actionContext), ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
            }
        };
    }
}
