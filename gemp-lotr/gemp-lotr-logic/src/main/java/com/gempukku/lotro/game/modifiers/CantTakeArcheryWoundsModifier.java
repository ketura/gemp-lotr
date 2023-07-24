package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class CantTakeArcheryWoundsModifier extends AbstractModifier {
    public CantTakeArcheryWoundsModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can't take archery wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
    }

    public CantTakeArcheryWoundsModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't take archery wounds", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
    }

    @Override
    public boolean canTakeArcheryWound(DefaultGame game, LotroPhysicalCard physicalCard) {
        return false;
    }
}
