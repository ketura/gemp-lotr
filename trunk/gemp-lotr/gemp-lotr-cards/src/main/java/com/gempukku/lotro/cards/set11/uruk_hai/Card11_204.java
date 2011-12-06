package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 6
 * Type: Minion • Uruk-Hai
 * Strength: 14
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Toil 1. (For each [URUK-HAI] character you exert when playing this, its twilight cost is -1.)
 */
public class Card11_204 extends AbstractMinion {
    public Card11_204() {
        super(6, 14, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Tyrannical Uruk");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.TOIL, 1);
    }
}
