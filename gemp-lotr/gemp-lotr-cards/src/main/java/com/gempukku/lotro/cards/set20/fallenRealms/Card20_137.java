package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * 3
 * Southron Deadeye
 * Minion • Man
 * 8	2	4
 * Southron. Archer. Ambush (1).
 * http://lotrtcg.org/coreset/fallenrealms/southrondeadeye(r1).png
 */
public class Card20_137 extends AbstractMinion {
    public Card20_137() {
        super(3, 8, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Deadeye");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 1);
        addKeyword(Keyword.ARCHER);
    }
}
