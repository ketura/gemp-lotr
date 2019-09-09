package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.ActionContext;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ModifierSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.PossessionClassSpotModifier;
import org.json.simple.JSONObject;

public class ClassSpot implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "class");

        final PossessionClass spotClass = FieldUtils.getEnum(PossessionClass.class, object.get("class"), "class");

        return new ModifierSource() {
            @Override
            public Modifier getModifier(ActionContext actionContext) {
                return new PossessionClassSpotModifier(actionContext.getSource(), spotClass);
            }
        };
    }
}
