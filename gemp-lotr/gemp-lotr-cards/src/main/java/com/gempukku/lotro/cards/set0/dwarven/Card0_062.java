package com.gempukku.lotro.cards.set0.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Promotional
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Damage +1.
 */
public class Card0_062 extends AbstractCompanion {
    public Card0_062() {
        super(2, 7, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Gimli", "Dwarven Delegate", true);
        addKeyword(Keyword.DAMAGE, 1);
    }
}
