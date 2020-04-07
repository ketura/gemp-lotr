package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.ModifierSource;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.CantDiscardFromPlayByPlayerModifier;
import org.json.simple.JSONObject;

public class OpponentMayNotDiscard implements ModifierSourceProducer {
    @Override
    public ModifierSource getModifierSource(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter");

        final String filter = FieldUtils.getString(object.get("filter"), "filter");
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return (actionContext) -> {
                    return new CantDiscardFromPlayByPlayerModifier(actionContext.getSource(), "Can't be discarded by opponent",
                            filterableSource.getFilterable(actionContext),
                            actionContext.getPerformingPlayer());
        };
    }
}
