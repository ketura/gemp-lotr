package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Game Text: Ring-bound. Each other companion is resistance +1.
 */
public class Card11_172 extends AbstractCompanion {
    public Card11_172() {
        super(2, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, null, "Sam", "Steadfast Friend", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, Filters.and(CardType.COMPANION, Filters.not(self)), 1);
    }
}
