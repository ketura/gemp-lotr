package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantTakeArcheryWoundsModifier extends AbstractModifier {
    public CantTakeArcheryWoundsModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can't take archery wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
    }

    public CantTakeArcheryWoundsModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't take archery wounds", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
    }

    @Override
    public boolean canTakeArcheryWound(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return false;
    }
}
