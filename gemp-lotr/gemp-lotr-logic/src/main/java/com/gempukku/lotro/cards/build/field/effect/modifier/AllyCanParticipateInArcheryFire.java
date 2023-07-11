package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.game.modifiers.AllyParticipatesInArcheryFireModifier;
import org.json.simple.JSONObject;

public class AllyCanParticipateInArcheryFire implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter");

        final String filter = FieldUtils.getString(object.get("filter"), "filter");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return actionContext -> new AllyParticipatesInArcheryFireModifier(actionContext.getSource(),
        		filterableSource.getFilterable(actionContext));
    }
}
