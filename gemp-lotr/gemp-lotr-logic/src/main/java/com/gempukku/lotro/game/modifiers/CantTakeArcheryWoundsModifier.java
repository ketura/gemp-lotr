package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;

public class CantTakeArcheryWoundsModifier extends AbstractModifier {
    public CantTakeArcheryWoundsModifier(PhysicalCard source, Filterable affectFilter) {
        super(source, "Can't take archery wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
    }

    public CantTakeArcheryWoundsModifier(PhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't take archery wounds", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
    }

    @Override
    public boolean canTakeArcheryWound(DefaultGame game, PhysicalCard physicalCard) {
        return false;
    }
}
