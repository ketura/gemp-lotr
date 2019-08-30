package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.CardStackedCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Staunch Defenders
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1C29
 * Game Text: While you can spot 3 cards stacked on this condition, Dwarves may not be exerted by Shadow cards.
 */
public class Card40_029 extends AbstractPermanent{
    public Card40_029() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, "Staunch Defenders", null, true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        CantExertWithCardModifier modifier = new CantExertWithCardModifier(
                self, Race.DWARF, new CardStackedCondition(3, self, Filters.any), Side.SHADOW);
        return Collections.singletonList(modifier);
    }
}
