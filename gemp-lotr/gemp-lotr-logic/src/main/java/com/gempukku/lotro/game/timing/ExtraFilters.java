package com.gempukku.lotro.game.timing;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayUtils;

public class ExtraFilters {
    public static Filter attachableTo(final DefaultGame game, final Filterable... filters) {
        return attachableTo(game, 0, filters);
    }

    public static Filter attachableTo(final DefaultGame game, final int twilightModifier, final Filterable... filters) {
        return Filters.and(Filters.playable(game, twilightModifier),
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, PhysicalCard physicalCard) {
                        if (physicalCard.getBlueprint().getValidTargetFilter(physicalCard.getOwner(), game, physicalCard) == null)
                            return false;
                        return PlayUtils.checkPlayRequirements(game, physicalCard, Filters.and(filters), 0, twilightModifier, false, false, true);
                    }
                });
    }
}
