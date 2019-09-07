package com.gempukku.lotro.cards.build.field.effect.requirement;

import com.gempukku.lotro.cards.build.*;
import com.gempukku.lotro.cards.build.field.FieldUtils;
import com.gempukku.lotro.common.Filterable;
import org.json.simple.JSONObject;

public class WasAssignedToSkirmish implements RequirementProducer {
    @Override
    public Requirement getPlayRequirement(JSONObject object, CardGenerationEnvironment environment) throws InvalidCardDefinitionException {
        FieldUtils.validateAllowedFields(object, "filter");

        final String filter = FieldUtils.getString(object.get("filter"), "filter");

        final FilterableSource filterableSource = environment.getFilterFactory().generateFilter(filter, environment);

        return actionContext -> {
            final Filterable filterable = filterableSource.getFilterable(actionContext);
            return actionContext.getGame().getActionsEnvironment().wasAssignedThisTurn(actionContext.getGame(), filterable);
        };
    }
}
