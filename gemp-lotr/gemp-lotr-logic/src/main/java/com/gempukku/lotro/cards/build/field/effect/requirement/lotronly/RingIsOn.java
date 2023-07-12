package com.gempukku.lotro.cards.build.field.effect.requirement.lotronly;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.effect.requirement.RequirementProducer;
import org.json.simple.JSONObject;

public class RingIsOn implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) {
        return (actionContext) -> actionContext.getGame().getGameState().isWearingRing();
    }
}
