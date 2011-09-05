package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantDiscardFromPlayModifier extends AbstractModifier {
    private Filter _sourceFilter;

    public CantDiscardFromPlayModifier(PhysicalCard source, String text, Filter affectFilter, Filter sourceFilter) {
        super(source, text, affectFilter, new ModifierEffect[]{ModifierEffect.DISCARD_FROM_PLAY_MODIFIER});
        _sourceFilter = sourceFilter;
    }

    @Override
    public boolean canBeDiscardedFromPlay(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, PhysicalCard source, boolean result) {
        if (_sourceFilter.accepts(gameState, modifiersQuerying, source))
            return false;
        return result;
    }
}
