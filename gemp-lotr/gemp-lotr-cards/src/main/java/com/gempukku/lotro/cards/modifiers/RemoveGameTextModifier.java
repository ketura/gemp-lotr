package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class RemoveGameTextModifier extends AbstractModifier {
    public RemoveGameTextModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public RemoveGameTextModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Has it's game text removed", affectFilter, condition, ModifierEffect.TEXT_MODIFIER);
    }

    @Override
    public boolean hasRemovedText(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
        return true;
    }
}
