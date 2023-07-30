package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;

public class CantRemoveThreatsModifier extends AbstractModifier {

    public CantRemoveThreatsModifier(LotroPhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't remove threats", Filters.and(sourceFilters), condition, ModifierEffect.THREAT_MODIFIER);
    }

    @Override
    public boolean canRemoveThreat(DefaultGame game, LotroPhysicalCard source) {
        return false;
    }
}
