package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 6
 * Game Text: Damage +1.
 */
public class Card10_081 extends AbstractMinion {
    public Card10_081() {
        super(3, 9, 2, 6, Race.URUK_HAI, Culture.SAURON, "Cirith Ungol Guard");
        addKeyword(Keyword.DAMAGE, 1);
    }
}
