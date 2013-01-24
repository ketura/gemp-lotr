package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * 4
 * Southron Deadeye
 * Fallen Realms	Minion • Man
 * 7	3	4
 * Southron. Ambush (2). Archer.
 */
public class Card20_137 extends AbstractMinion {
    public Card20_137() {
        super(4, 7, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Deadeye");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 2);
        addKeyword(Keyword.ARCHER);
    }
}
