package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Damage +1.
 */
public class Card1_131 extends AbstractMinion {
    public Card1_131() {
        super(5, 10, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Orthanc Assassin", true);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.DAMAGE);
    }
}
