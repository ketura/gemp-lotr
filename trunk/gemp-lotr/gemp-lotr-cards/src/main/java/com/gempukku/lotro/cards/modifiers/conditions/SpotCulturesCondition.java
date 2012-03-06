package com.gempukku.lotro.cards.modifiers.conditions;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.HashSet;
import java.util.Set;

public class SpotCulturesCondition implements Condition {
    private int _count;
    private Filterable _filters;

    public SpotCulturesCondition(int count, Filterable filters) {
        _count = count;
        _filters = filters;
    }

    @Override
    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
        Set<Culture> cultures = new HashSet<Culture>();
        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, _filters)) {
            final Culture culture = physicalCard.getBlueprint().getCulture();
            if (cultures.add(culture))
                if (cultures.size() >= _count)
                    return true;
        }

        return cultures.size() >= _count;
    }
}
