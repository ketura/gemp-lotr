package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.DefaultActionContext;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.DefaultGame;
import org.json.simple.JSONObject;

public class IsOwnerRequirementProducer implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object);

        return new Requirement() {
            @Override
            public boolean accepts(DefaultActionContext<DefaultGame> actionContext) {
                return actionContext.getPerformingPlayer().equals(actionContext.getSource().getOwner());
            }
        };
    }
}
