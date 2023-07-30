package com.gempukku.lotro.filters;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.rules.lotronly.LotroPlayUtils;

public class ExtraFilters {
    public static Filter attachableTo(final DefaultGame game, final Filterable... filters) {
        return attachableTo(game, 0, filters);
    }

    public static Filter attachableTo(final DefaultGame game, final int twilightModifier, final Filterable... filters) {
        return Filters.and(Filters.playable(game, twilightModifier),
                new Filter() {
                    @Override
                    public boolean accepts(DefaultGame game, LotroPhysicalCard physicalCard) {
                        if (physicalCard.getBlueprint().getValidTargetFilter(physicalCard.getOwner(), game, physicalCard) == null)
                            return false;
                        return LotroPlayUtils.checkPlayRequirements(game, physicalCard, Filters.and(filters), 0, twilightModifier, false, false, true);
                    }
                });
    }
}
