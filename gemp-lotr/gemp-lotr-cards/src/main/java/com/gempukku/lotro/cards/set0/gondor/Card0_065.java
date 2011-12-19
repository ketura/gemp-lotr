package com.gempukku.lotro.cards.set0.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Promotional
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Knight. Ranger. Ring-bound.
 */
public class Card0_065 extends AbstractCompanion {
    public Card0_065() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, null, "Boromir", true);
        addKeyword(Keyword.KNIGHT);
        addKeyword(Keyword.RANGER);
        addKeyword(Keyword.RING_BOUND);
    }
}
