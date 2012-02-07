package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 14
 * Vitality: 3
 * Site: 5
 * Game Text: Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 */
public class Card15_171 extends AbstractMinion {
    public Card15_171() {
        super(5, 14, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Tracking Uruk");
        addKeyword(Keyword.HUNTER, 1);
    }
}
