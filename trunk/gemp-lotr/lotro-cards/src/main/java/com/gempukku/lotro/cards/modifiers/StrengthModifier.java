package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class StrengthModifier extends AbstractModifier {
    private int _modifier;

    public StrengthModifier(PhysicalCard source, String text, Filter affectFilter, int modifier) {
        super(source, text, affectFilter);
        _modifier = modifier;
    }

    @Override
    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
        if (affectsCard(gameState, modifiersQuerying, physicalCard))
            return result + _modifier;
        return result;
    }
}
