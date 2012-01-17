package com.gempukku.lotro.cards.set15.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Damage +1. Hunter 3. (While skirmishing a non-hunter character, this character is strength +3.)
 */
public class Card15_005 extends AbstractCompanion {
    public Card15_005() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Gimli", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.HUNTER, 3);
    }
}
