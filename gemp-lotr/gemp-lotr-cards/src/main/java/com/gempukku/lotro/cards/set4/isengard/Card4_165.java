package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1.
 */
public class Card4_165 extends AbstractMinion {
    public Card4_165() {
        super(2, 7, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Othanc Warrior");
        addKeyword(Keyword.DAMAGE);
    }
}
