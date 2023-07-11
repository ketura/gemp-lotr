package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class RemoveGameTextModifier extends AbstractModifier {
    public RemoveGameTextModifier(PhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public RemoveGameTextModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Has it's game text removed", affectFilter, condition, ModifierEffect.TEXT_MODIFIER);
    }

    @Override
    public boolean hasRemovedText(DefaultGame game, PhysicalCard physicalCard) {
        return true;
    }
}
