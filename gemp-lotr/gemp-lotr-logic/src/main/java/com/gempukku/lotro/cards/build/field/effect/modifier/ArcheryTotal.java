package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import org.json.simple.JSONObject;

public class ArcheryTotal implements EffectProcessor {
    @Override
    public void processEffect(JSONObject effectObject, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "amount", "condition", "side");

        final JSONObject[] conditionArray = FieldUtils.getObjectArray(effectObject.get("condition"), "condition");
        final int amount = FieldUtils.getInteger(effectObject.get("amount"), "amount");
        final Side side = FieldUtils.getEnum(Side.class, effectObject.get("side"), "side");

        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                new ModifierSource() {
                    @Override
                    public Modifier getModifier(LotroGame game, PhysicalCard self) {
                        return new ArcheryTotalModifier(self, side,
                                new RequirementCondition(requirements, null, self, null, null),
                                amount);
                    }
                });
    }
}
