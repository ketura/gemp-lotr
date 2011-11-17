package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantRemoveThreatsModifier extends AbstractModifier {
    private Condition _condition;
    private Filterable[] _sourceFilters;

    public CantRemoveThreatsModifier(PhysicalCard source, Condition condition, Filterable... sourceFilters) {
        super(source, "Can't remove threats", null, condition, ModifierEffect.BURDEN_MODIFIER);
        _condition = condition;
        _sourceFilters = sourceFilters;
    }

    @Override
    public boolean canRemoveThreat(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard source) {
        if ((_condition == null || _condition.isFullfilled(gameState, modifiersQuerying))
                && Filters.and(_sourceFilters).accepts(gameState, modifiersQuerying, source))
            return false;
        return true;
    }
}
