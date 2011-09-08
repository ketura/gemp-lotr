package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: Archer.
 */
public class Card1_176 extends AbstractMinion {
    public Card1_176() {
        super(3, 7, 1, 4, Keyword.ORC, Culture.MORIA, "Goblin Marksman");
        addKeyword(Keyword.ARCHER);
    }
}
