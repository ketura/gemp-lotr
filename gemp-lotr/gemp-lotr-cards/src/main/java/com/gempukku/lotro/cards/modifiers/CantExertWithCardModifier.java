package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;

public class CantExertWithCardModifier extends AbstractModifier {
    private Filter _preventExertWithFilter;

    public CantExertWithCardModifier(PhysicalCard source, Filter affectFilter, Filter preventExertWithFilter) {
        super(source, "Affected by exertion preventing effect", affectFilter, new ModifierEffect[]{ModifierEffect.WOUND_MODIFIER});
        _preventExertWithFilter = preventExertWithFilter;
    }

    @Override
    public boolean canBeExerted(GameState gameState, ModifiersLogic modifiersLogic, PhysicalCard source, PhysicalCard card, boolean result) {
        if (_preventExertWithFilter.accepts(gameState, modifiersLogic, source))
            return false;
        return result;
    }
}
