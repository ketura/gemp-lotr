package com.gempukku.lotro.cards.build.field.effect;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Phase;
import org.json.simple.JSONObject;

public class PlayedInOtherPhase implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "phase", "condition");

        final Phase phase = FieldUtils.getEnum(Phase.class, value.get("phase"), "phase");
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");

        final Requirement[] conditions = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendPlayInOtherPhaseCondition(
                new Requirement() {
                    @Override
                    public boolean accepts(ActionContext actionContext) {
                        if (actionContext.getGame().getGameState().getCurrentPhase() != phase)
                            return false;

                        for (Requirement condition : conditions) {
                            if (!condition.accepts(actionContext))
                                return false;
                        }

                        return true;
                    }
                }
        );
    }
}
