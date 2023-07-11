package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantRemoveThreatsModifier extends AbstractModifier {

    public CantRemoveThreatsModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't remove threats", Filters.and(sourceFilters), condition, ModifierEffect.THREAT_MODIFIER);
    }

    @Override
    public boolean canRemoveThreat(DefaultGame game, PhysicalCard source) {
        return false;
    }
}
