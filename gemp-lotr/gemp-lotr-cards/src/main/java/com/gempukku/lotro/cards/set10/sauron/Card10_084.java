package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 6
 * Game Text: Damage +1.
 */
public class Card10_084 extends AbstractMinion {
    public Card10_084() {
        super(2, 8, 2, 6, Race.URUK_HAI, Culture.SAURON, "Cirith Ungol Sentry");
        addKeyword(Keyword.DAMAGE, 1);
    }
}
