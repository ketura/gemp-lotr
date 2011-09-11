package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 5
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1.
 */
public class Card1_151 extends AbstractMinion {
    public Card1_151() {
        super(2, 5, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Savage");
        addKeyword(Keyword.DAMAGE);
    }
}
