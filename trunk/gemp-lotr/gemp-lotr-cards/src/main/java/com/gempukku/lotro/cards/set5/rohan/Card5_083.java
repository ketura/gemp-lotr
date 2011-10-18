package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant.
 */
public class Card5_083 extends AbstractCompanion {
    public Card5_083() {
        super(2, 6, 3, Culture.ROHAN, Race.MAN, null, "Household Guard");
        addKeyword(Keyword.VALIANT);
    }
}
