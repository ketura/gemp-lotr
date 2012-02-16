package com.gempukku.lotro.cards.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class CantBeTransferredModifier extends AbstractModifier {
    public CantBeTransferredModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public CantBeTransferredModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Can't be transferred", affectFilter, condition, ModifierEffect.TRANSFER_MODIFIER);
    }

    @Override
    public boolean canBeTransferred(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard attachment) {
        return false;
    }
}
