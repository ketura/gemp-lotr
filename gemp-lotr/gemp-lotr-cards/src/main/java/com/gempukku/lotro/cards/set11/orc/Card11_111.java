package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 13
 * Vitality: 2
 * Site: 4
 * Game Text: Toil 2. (For each [ORC] character you exert when playing this, its twilight cost is -2.)
 */
public class Card11_111 extends AbstractMinion {
    public Card11_111() {
        super(5, 13, 2, 4, Race.ORC, Culture.ORC, "Champion Orc");
        addKeyword(Keyword.TOIL, 2);
    }
}
