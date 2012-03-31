package com.gempukku.lotro.cards.set14.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * Set: Expanded Middle-earth
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Half•troll
 * Strength: 9
 * Vitality: 3
 * Site: 4
 * Game Text: Ambush (1). Damage +1. Fierce. Lurker. Muster. Toil 2.
 */
public class Card14_013 extends AbstractMinion {
    public Card14_013() {
        super(4, 9, 3, 4, Race.HALF_TROLL, Culture.ORC, "Horror of Harad", null, true);
        addKeyword(Keyword.AMBUSH, 1);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.LURKER);
        addKeyword(Keyword.MUSTER);
        addKeyword(Keyword.TOIL, 2);
    }
}
