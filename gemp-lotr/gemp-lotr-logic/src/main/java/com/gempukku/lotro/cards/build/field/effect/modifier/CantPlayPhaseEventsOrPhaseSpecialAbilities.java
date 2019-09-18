package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.PlayersCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier;
import org.json.simple.JSONObject;

public class CantPlayPhaseEventsOrPhaseSpecialAbilities implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "phase", "condition");

        final Phase phase = FieldUtils.getEnum(Phase.class, object.get("phase"), "phase");
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("condition"), "condition");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(ActionContext actionContext) {
                return new PlayersCantPlayPhaseEventsOrPhaseSpecialAbilitiesModifier(actionContext.getSource(),
                        new RequirementCondition(requirements, actionContext), phase);
            }
        };
    }
}
