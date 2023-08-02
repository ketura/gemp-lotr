package com.gempukku.lotro.cards.build.field.effect.modifier.lotronly;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.modifier.ModifierSourceProducer;
import com.gempukku.lotro.cards.build.field.effect.modifier.RequirementCondition;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Modifier;
import com.gempukku.lotro.modifiers.ModifierFlag;
import com.gempukku.lotro.modifiers.SpecialFlagModifier;
import org.json.simple.JSONObject;

public class CantPreventWounds implements ModifierSourceProducer {

    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object,"requires");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("requires"), "requires");
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(DefaultActionContext<DefaultGame> actionContext) {
                return new SpecialFlagModifier(actionContext.getSource(),
                        new RequirementCondition(requirements, actionContext), ModifierFlag.CANT_PREVENT_WOUNDS);
            }
        };
    }
}
