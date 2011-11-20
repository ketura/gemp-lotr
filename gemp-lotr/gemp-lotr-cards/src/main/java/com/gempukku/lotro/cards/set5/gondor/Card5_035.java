package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Knight.
 */
public class Card5_035 extends AbstractCompanion {
    public Card5_035() {
        super(2, 6, 3, 6, Culture.GONDOR, Race.MAN, null, "Gondorian Knight");
        addKeyword(Keyword.KNIGHT);
    }
}
