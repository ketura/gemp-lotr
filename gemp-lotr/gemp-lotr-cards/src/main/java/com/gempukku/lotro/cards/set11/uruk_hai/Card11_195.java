package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Muster. (At the start of the regroup phase, you may discard a card from hand to draw a card.)
 */
public class Card11_195 extends AbstractMinion {
    public Card11_195() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Murderous Uruk");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.MUSTER);
    }
}
