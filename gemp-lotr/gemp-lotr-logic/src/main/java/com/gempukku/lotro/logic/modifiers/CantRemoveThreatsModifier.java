package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

public class CantRemoveThreatsModifier extends AbstractModifier {

    public CantRemoveThreatsModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't remove threats", Filters.and(sourceFilters), condition, ModifierEffect.THREAT_MODIFIER);
    }

    @Override
    public boolean canRemoveThreat(LotroGame game, PhysicalCard source) {
        return false;
    }
}
