package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 14
 * Vitality: 3
 * Site: 4
 * Game Text: Toil 2. (For each [MEN] character you exert when playing this, its twilight cost is -2.)
 */
public class Card11_088 extends AbstractMinion {
    public Card11_088() {
        super(6, 14, 3, 4, Race.MAN, Culture.MEN, "Legion of Harad");
        addKeyword(Keyword.TOIL, 2);
    }
}
