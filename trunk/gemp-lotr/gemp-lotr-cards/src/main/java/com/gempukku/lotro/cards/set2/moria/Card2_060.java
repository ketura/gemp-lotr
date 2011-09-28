package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 4
 * Vitality: 1
 * Site: 4
 * Game Text: Archer.
 */
public class Card2_060 extends AbstractMinion {
    public Card2_060() {
        super(2, 4, 1, 4, Race.ORC, Culture.MORIA, "Goblin Bowman");
        addKeyword(Keyword.ARCHER);
    }
}
