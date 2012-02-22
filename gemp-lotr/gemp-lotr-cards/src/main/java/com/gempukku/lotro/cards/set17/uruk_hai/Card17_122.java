package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 5
 * Vitality: 2
 * Site: 5
 * Game Text: Hunter 6 (While skirmishing a non-hunter character, this character is strength +6.)
 */
public class Card17_122 extends AbstractMinion {
    public Card17_122() {
        super(4, 5, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Butcher");
        addKeyword(Keyword.HUNTER, 6);
    }
}
