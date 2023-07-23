package com.gempukku.lotro.game.rules.tribbles;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.rules.OptionalTriggersRule;

public class TribblesOptionalTriggersRule extends OptionalTriggersRule {

    public TribblesOptionalTriggersRule(DefaultActionsEnvironment actionsEnvironment) {
        super(actionsEnvironment);
    }

    private Filter getActivatableCardsFilter(String playerId) {
        return Filters.and(Filters.owner(playerId), Filters.active);
    }
}