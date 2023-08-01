package com.gempukku.lotro.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.condition.Condition;

public class RemoveGameTextModifier extends AbstractModifier {
    public RemoveGameTextModifier(LotroPhysicalCard source, Filterable affectFilter) {
        this(source, null, affectFilter);
    }

    public RemoveGameTextModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Has it's game text removed", affectFilter, condition, ModifierEffect.TEXT_MODIFIER);
    }

    @Override
    public boolean hasRemovedText(DefaultGame game, LotroPhysicalCard physicalCard) {
        return true;
    }
}
