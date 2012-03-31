package com.gempukku.lotro.cards.set18.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 3
 * Resistance: 7
 * Game Text: While the fellowship is in region 1 and you can spot 2 other [SHIRE] companions, Fredegar Bolger
 * is strength +3.
 */
public class Card18_107 extends AbstractCompanion {
    public Card18_107() {
        super(1, 3, 3, 7, Culture.SHIRE, Race.HOBBIT, null, "Fredegar Bolger", "Fatty", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self,
                new AndCondition(
                        new LocationCondition(Filters.region(1)),
                        new SpotCondition(2, Filters.not(self), Culture.SHIRE, CardType.COMPANION)
                ), 3);
    }
}
