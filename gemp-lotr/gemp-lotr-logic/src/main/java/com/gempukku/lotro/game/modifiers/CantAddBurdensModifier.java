package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantAddBurdensModifier extends AbstractModifier {
    public CantAddBurdensModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't add burdens", Filters.and(sourceFilters), condition, ModifierEffect.BURDEN_MODIFIER);
    }

    @Override
    public boolean canAddBurden(DefaultGame game, String performingPlayer, PhysicalCard source) {
        return false;
    }
}
