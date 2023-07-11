package com.gempukku.lotro.game.modifiers.condition;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.modifiers.Condition;

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
    public boolean isFullfilled(DefaultGame game) {
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
