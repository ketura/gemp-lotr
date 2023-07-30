package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class CantBeTransferredModifier extends AbstractModifier {
    public CantBeTransferredModifier(LotroPhysicalCard source, Filterable affectFilter) {
        this(source, affectFilter, null);
    }

    public CantBeTransferredModifier(LotroPhysicalCard source, Filterable affectFilter, Condition condition) {
        super(source, "Can't be transferred", affectFilter, condition, ModifierEffect.TRANSFER_MODIFIER);
    }

    @Override
    public boolean canBeTransferred(DefaultGame game, LotroPhysicalCard attachment) {
        return false;
    }
}
