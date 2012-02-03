package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class CantDiscardFromPlayModifier extends AbstractModifier {
    private Filterable _sourceFilter;

    public CantDiscardFromPlayModifier(PhysicalCard source, String text, Filterable affectFilter, Filterable sourceFilter) {
        super(source, text, affectFilter, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _sourceFilter = sourceFilter;
    }

    public CantDiscardFromPlayModifier(PhysicalCard source, String text, Condition condition, Filterable affectFilter, Filterable sourceFilter) {
        super(source, text, affectFilter, condition, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _sourceFilter = sourceFilter;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source) {
        if (Filters.and(_sourceFilter).accepts(gameState, modifiersQuerying, source))
            return false;
        return true;
    }
}
