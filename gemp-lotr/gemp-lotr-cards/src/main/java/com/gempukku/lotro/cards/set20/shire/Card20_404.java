package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.AndCondition;

/**
 * 0
 * A Promise
 * Shire	Condition • Support Area
 * While you can spot Frodo and Sam and neither are exhausted, each are strength +1.
 */
public class Card20_404 extends AbstractPermanent {
    public Card20_404() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, "A Promise");
    }

    @Override
    public java.util.List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return java.util.Collections.singletonList(new StrengthModifier(self, Filters.or(Filters.frodo, Filters.sam),
new AndCondition(
new SpotCondition(Filters.frodo, Filters.not(Filters.exhausted)),
new SpotCondition(Filters.sam, Filters.not(Filters.exhausted))), 1));
}
}
