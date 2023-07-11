package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantBeTransferredModifier extends AbstractModifier {
    public CantBeTransferredModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public CantBeTransferredModifier(PhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Can't be transferred", affectFilter, condition, ModifierEffect.TRANSFER_MODIFIER);
    }

    @Override
    public boolean canBeTransferred(DefaultGame game, PhysicalCard attachment) {
        return false;
    }
}
