package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ExtraFilters {
    public static Filter attachableTo(final LotroGame game, final Filterable... filters) {
        return attachableTo(game, 0, filters);
    }

    public static Filter attachableTo(final LotroGame game, final int twilightModifier, final Filterable... filters) {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                if (!(physicalCard.getBlueprint() instanceof AbstractAttachable))
                    return false;
                AbstractAttachable weapon = (AbstractAttachable) physicalCard.getBlueprint();
                return weapon.checkPlayRequirements(physicalCard.getOwner(), game, physicalCard, Filters.and(filters), twilightModifier);
            }
        };
    }
}
