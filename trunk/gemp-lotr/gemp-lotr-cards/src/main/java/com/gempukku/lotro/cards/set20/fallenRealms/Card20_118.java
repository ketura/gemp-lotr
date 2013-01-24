package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * 5
 * Easterling Regiment
 * Fallen Realms	Minion • Man
 * 11	3	4
 * Easterling. Toil 1. Fierce.
 */
public class Card20_118 extends AbstractMinion {
    public Card20_118() {
        super(5, 11, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Regiment");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
        addKeyword(Keyword.FIERCE);
    }
}
