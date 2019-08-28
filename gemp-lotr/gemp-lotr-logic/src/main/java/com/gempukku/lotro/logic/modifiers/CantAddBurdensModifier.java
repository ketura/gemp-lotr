package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantAddBurdensModifier extends AbstractModifier {
    public CantAddBurdensModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't add burdens", Filters.and(sourceFilters), condition, ModifierEffect.BURDEN_MODIFIER);
    }

    @Override
    public boolean canAddBurden(LotroGame game, String performingPlayer, PhysicalCard source) {
        return false;
    }
}
