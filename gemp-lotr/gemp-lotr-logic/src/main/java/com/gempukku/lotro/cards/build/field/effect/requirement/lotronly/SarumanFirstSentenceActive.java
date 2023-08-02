package com.gempukku.lotro.cards.build.field.effect.requirement.lotronly;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.requirement.RequirementProducer;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.ModifierFlag;
import org.json.simple.JSONObject;

public class SarumanFirstSentenceActive implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object);

        return (actionContext) -> {
            final DefaultGame game = actionContext.getGame();
            return !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.SARUMAN_FIRST_SENTENCE_INACTIVE);
        };
    }
}
