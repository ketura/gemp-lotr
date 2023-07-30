package com.gempukku.lotro.cards.build.field.effect.modifier.lotronly;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.modifier.ModifierSourceProducer;
import com.gempukku.lotro.modifiers.CantReplaceSiteModifier;
import com.gempukku.lotro.modifiers.Modifier;
import org.json.simple.JSONObject;

public class CantReplaceSite implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter");

        final String filter = FieldUtils.getString(object.get("filter"), "filter", "any");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(ActionContext actionContext) {
                return new CantReplaceSiteModifier(actionContext.getSource(), null,
                        filterableSource.getFilterable(actionContext));
            }
        };
    }
}
