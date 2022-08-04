package com.gempukku.lotro.logic.modifiers.condition;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;

import java.util.HashSet;
import java.util.Set;

public class SpotCulturesCondition implements Condition {
    private final int _count;
    private final Filterable _filters;

    public SpotCulturesCondition(int count, Filterable filters) {
        _count = count;
        _filters = filters;
    }

    @Override
    public boolean isFullfilled(LotroGame game) {
        Set<Culture> cultures = new HashSet<>();
        for (PhysicalCard physicalCard : Filters.filterActive(game, _filters)) {
            final Culture culture = physicalCard.getBlueprint().getCulture();
            if (cultures.add(culture))
                if (cultures.size() >= _count)
                    return true;
        }

        return cultures.size() >= _count;
    }
}
