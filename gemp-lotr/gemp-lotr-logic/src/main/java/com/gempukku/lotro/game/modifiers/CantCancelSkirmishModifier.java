package com.gempukku.lotro.game.modifiers;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;

public class CantCancelSkirmishModifier extends AbstractModifier {

    public CantCancelSkirmishModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can't cancel skirmish", affectFilter, ModifierEffect.CANCEL_SKIRMISH_MODIFIER);
    }

    public CantCancelSkirmishModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't cancel skirmish", affectFilter, condition, ModifierEffect.CANCEL_SKIRMISH_MODIFIER);
    }

    @Override
    public boolean canCancelSkirmish(DefaultGame game, LotroPhysicalCard card) {
        return false;
    }
}
