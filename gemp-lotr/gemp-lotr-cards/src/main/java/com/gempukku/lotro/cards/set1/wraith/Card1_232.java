package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Each companion or ally who bears a [WRAITH] condition is strength -2.
 */
public class Card1_232 extends AbstractMinion {
    public Card1_232() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Lemenya", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self,
                Filters.and(
                        Filters.or(
                                Filters.type(CardType.ALLY),
                                Filters.type(CardType.COMPANION)
                        ),
                        Filters.hasAttached(
                                Filters.and(
                                        Filters.culture(Culture.WRAITH),
                                        Filters.type(CardType.CONDITION)
                                )
                        )
                ), -2);
    }
}
