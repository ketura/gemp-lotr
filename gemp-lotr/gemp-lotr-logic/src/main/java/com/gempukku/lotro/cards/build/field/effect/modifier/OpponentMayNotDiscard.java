package com.gempukku.lotro.cards.build.field.effect.modifier;

import com.gempukku.lotro.cards.build.BuiltLotroCardBlueprint;
import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.field.EffectProcessor;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.logic.modifiers.CantDiscardFromPlayByPlayerModifier;
import org.json.simple.JSONObject;

public class OpponentMayNotDiscard implements EffectProcessor {
    @Override
    public void processEffect(JSONObject effectObject, BuiltLotroCardBlueprint blueprint, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(effectObject, "filter");

        final String filter = FieldUtils.getString(effectObject.get("filter"), "filter", "self");
        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);

        blueprint.appendInPlayModifier(
                (game, self) -> new CantDiscardFromPlayByPlayerModifier(self, "Can't be discarded by opponent",
                        filterableSource.getFilterable(null, game, self, null, null),
                        self.getOwner()));
    }
}
