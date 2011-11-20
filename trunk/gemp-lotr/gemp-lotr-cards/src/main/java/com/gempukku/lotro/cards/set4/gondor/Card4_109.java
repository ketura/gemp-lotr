package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Defender +1.
 */
public class Card4_109 extends AbstractCompanion {
    public Card4_109() {
        super(4, 8, 4, 6, Culture.GONDOR, Race.MAN, Signet.GANDALF, "Aragorn", true);
        addKeyword(Keyword.DEFENDER, 1);
    }
}
