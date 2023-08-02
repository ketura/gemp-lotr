package com.gempukku.lotro.cards.build.field.effect.requirement.lotronly;

import com.gempukku.lotro.cards.build.CardGenerationEnvironment;
import com.gempukku.lotro.cards.build.FilterableSource;
import com.gempukku.lotro.cards.build.InvalidCardDefinitionException;
import com.gempukku.lotro.cards.build.Requirement;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.cards.build.field.effect.requirement.RequirementProducer;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import org.json.simple.JSONObject;

public class CanSpotWounds implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter", "amount");

        final String filter = FieldUtils.getString(object.get("filter"), "filter");
        final int count = FieldUtils.getInteger(object.get("amount"), "amount", 1);

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return actionContext -> {
            final Filterable filterable = filterableSource.getFilterable(actionContext);

            for (LotroPhysicalCard physicalCard : Filters.filterActive(actionContext.getGame(), filterable)) {
                if (actionContext.getGame().getGameState().getWounds(physicalCard) >= count) {
                    return true;
                }
            }
            return false;
        };
    }
}
