package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Game Text: Valiant. While the Ringbearer is assigned to a skirmish, each [ROHAN] companion gains hunter 1.
 */
public class Card17_093 extends AbstractCompanion {
    public Card17_093() {
        super(4, 8, 4, 6, Culture.ROHAN, Race.MAN, null, "Aragorn", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(Culture.ROHAN, CardType.COMPANION),
                new SpotCondition(Filters.ringBearer, Filters.assignedToSkirmish), Keyword.HUNTER, 1);
    }
}
