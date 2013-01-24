package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * 2
 * Easterling Scout
 * Fallen Realms	Minion • Man
 * 6	1	4
 * Easterling. Toil 1.
 */
public class Card20_119 extends AbstractMinion {
    public Card20_119() {
        super(2, 6, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Scout");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }
}
