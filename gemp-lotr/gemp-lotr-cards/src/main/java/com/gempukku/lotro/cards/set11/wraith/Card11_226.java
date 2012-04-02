package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. Toil 2. (For each [WRAITH] character you exert when playing this, its twilight cost is -2.)
 * Muster. (At the start of the regroup phase, you may discard a card from hand to draw a card.)
 */
public class Card11_226 extends AbstractMinion {
    public Card11_226() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.witchKing, "Captain of the Nine Riders", true);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.TOIL, 2);
        addKeyword(Keyword.MUSTER);
    }
}
