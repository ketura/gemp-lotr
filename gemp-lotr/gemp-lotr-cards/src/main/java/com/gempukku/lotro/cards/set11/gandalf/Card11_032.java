package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: While you can spot a [GANDALF] Wizard, each companion who has resistance 6 or more is strength +1.
 */
public class Card11_032 extends AbstractPermanent {
    public Card11_032() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.GANDALF, "G for Grand");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.and(CardType.COMPANION, Filters.minResistance(6)), new SpotCondition(Culture.GANDALF, Race.WIZARD), 1));
}
}
