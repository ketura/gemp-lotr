package com.gempukku.lotro.cards.set12.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot a Hobbit, each unbound companion is resistance +1.
 */
public class Card12_125 extends AbstractPermanent {
    public Card12_125() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, "Measure of Comfort");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ResistanceModifier(self, Filters.unboundCompanion, new SpotCondition(Race.HOBBIT), 1));
}
}
