package com.gempukku.lotro.logic.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;

public class CantDiscardFromPlayModifier extends AbstractModifier {
    private Filter _sourceFilter;

    public CantDiscardFromPlayModifier(PhysicalCard source, String text, Filter affectFilter, Filter sourceFilter) {
        super(source, text, affectFilter, ModifierEffect.DISCARD_FROM_PLAY_MODIFIER);
        _sourceFilter = sourceFilter;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source) {
        if (_sourceFilter.accepts(gameState, modifiersQuerying, source))
            return false;
        return true;
    }
}
