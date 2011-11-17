package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantExertWithCardModifier extends AbstractModifier {
    private Filterable _preventExertWithFilter;

    public CantExertWithCardModifier(PhysicalCard source, Filterable affectFilter, Filterable preventExertWithFilter) {
        this(source, affectFilter, null, preventExertWithFilter);
    }

    public CantExertWithCardModifier(PhysicalCard source, Filterable affectFilter, Condition condition, Filterable preventExertWithFilter) {
        super(source, "Affected by exertion preventing effect", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
        _preventExertWithFilter = preventExertWithFilter;
    }

    @Override
    public boolean canBeExerted(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard source, PhysicalCard card) {
        if (Filters.and(_preventExertWithFilter).accepts(gameState, modifiersQuerying, source))
            return false;
        return true;
    }
}
