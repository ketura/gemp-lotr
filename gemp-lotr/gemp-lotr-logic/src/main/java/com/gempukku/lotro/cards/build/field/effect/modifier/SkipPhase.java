package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Modifier;
import com.gempukku.lotro.modifiers.ShouldSkipPhaseModifier;
import org.json.simple.JSONObject;

public class SkipPhase implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "requires", "phase");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(object.get("requires"), "requires");
        final Phase phase = FieldUtils.getEnum(Phase.class, object.get("phase"), "phase");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(DefaultActionContext<DefaultGame> actionContext) {
                return new ShouldSkipPhaseModifier(actionContext.getSource(),
                        new RequirementCondition(requirements, actionContext), phase);
            }
        };
    }
}
