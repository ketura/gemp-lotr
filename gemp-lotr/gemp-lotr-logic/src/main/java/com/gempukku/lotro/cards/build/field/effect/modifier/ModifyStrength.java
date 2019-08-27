package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import org.json.simple.JSONObject;

public class ModifyStrength implements EffectProcessor {
    @Override
    public void processEffect(JSONObject value, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(value, "filter", "condition", "amount");

        final int amount = FieldUtils.getInteger(value.get("amount"), "amount");
        final JSONObject[] conditionArray = FieldUtils.getObjectArray(value.get("condition"), "condition");
        final String filter = FieldUtils.getString(value.get("filter"), "filter", "self");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
        final Requirement[] requirements = environment.getRequirementFactory().getRequirements(conditionArray, environment);

        blueprint.appendInPlayModifier(
                new ModifierSource() {
                    @Override
                    public Modifier getModifier(LotroGame game, PhysicalCard self) {
                        return new StrengthModifier(self,
                                filterableSource.getFilterable(null, game, self, null, null),
                                new RequirementCondition(requirements, null, self, null, null), amount);
                    }
                });
    }
}
