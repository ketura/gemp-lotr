package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantBeOverwhelmedModifier extends AbstractModifier {

    public CantBeOverwhelmedModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public CantBeOverwhelmedModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Cannot be overwhelmed", affectFilter, condition, ModifierEffect.OVERWHELM_MODIFIER);
    }

    @Override
    public int getOverwhelmMultiplier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return Integer.MAX_VALUE;
    }
}
