package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Minion • Man
 * Strength: 3
 * Vitality: 1
 * Site: 4
 * Game Text: Southron. Ambush (2).
 */
public class Card4_252 extends AbstractMinion {
    public Card4_252() {
        super(1, 3, 1, 4, Race.MAN, Culture.RAIDER, "Southron Scout");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 2);
    }
}
