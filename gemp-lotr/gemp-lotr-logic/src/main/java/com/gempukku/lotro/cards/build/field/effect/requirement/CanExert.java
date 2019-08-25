package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.PlayRequirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import org.json.simple.JSONObject;

public class CanExert implements RequirementProducer {
    @Override
    public PlayRequirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        final int count = FieldUtils.getInteger(object.get("count"), "count", 1);
        final int times = FieldUtils.getInteger(object.get("times"), "times", 1);
        final String filter = FieldUtils.getString(object.get("filter"), "filter");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter);
        return (game, self) -> {
            final Filterable filterable = filterableSource.getFilterable(null, game, self, null, null);
            return PlayConditions.canExert(self, game, times, count, filterable);
        };
    }
}
