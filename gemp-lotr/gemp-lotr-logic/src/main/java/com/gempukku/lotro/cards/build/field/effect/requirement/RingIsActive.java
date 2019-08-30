package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import org.json.simple.JSONObject;

public class RingIsActive implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object);

        return (actionContext, playerId, game, self, effectResult, effect) -> !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.RING_TEXT_INACTIVE);
    }
}
