package com.gempukku.lotro.cards.build.field.effect.modifier.lotronly;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.modifier.ModifierSourceProducer;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.Modifier;
import com.gempukku.lotro.modifiers.lotronly.MayNotBearModifier;
import org.json.simple.JSONObject;

public class CantBear implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter", "cardFilter");

        final String filter = FieldUtils.getString(object.get("filter"), "filter");
        final String cardFilter = FieldUtils.getString(object.get("cardFilter"), "cardFilter");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);
        final FilterableSource cardFilterSource = environment.getFilterFactory().generateFilter(cardFilter, environment);

        return new ModifierSource() {
            @Override
            public Modifier getModifier(DefaultActionContext<DefaultGame> actionContext) {
                return new MayNotBearModifier(actionContext.getSource(),
                        filterableSource.getFilterable(actionContext),
                        cardFilterSource.getFilterable(actionContext));
            }
        };
    }
}
