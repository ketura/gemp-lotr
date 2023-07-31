package com.gempukku.lotro.modifiers.lotronly;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.modifiers.AbstractModifier;
import com.gempukku.lotro.modifiers.Condition;
import com.gempukku.lotro.modifiers.ModifierEffect;

import java.util.Collection;

public class CantTakeWoundsModifier extends AbstractModifier {
    public CantTakeWoundsModifier(LotroPhysicalCard source, Filterable affectFilter) {
        super(source, "Can't take wounds", affectFilter, ModifierEffect.WOUND_MODIFIER);
    }

    public CantTakeWoundsModifier(LotroPhysicalCard source, Condition condition, Filterable affectFilter) {
        super(source, "Can't take wounds", affectFilter, condition, ModifierEffect.WOUND_MODIFIER);
    }

    @Override
    public boolean canTakeWounds(DefaultGame game, Collection<LotroPhysicalCard> woundSources, LotroPhysicalCard physicalCard, int woundsAlreadyTaken, int woundsToTake) {
        return false;
    }
}
