package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;

/**
 * 6
 * Easterling Detachment
 * Fallen Realms	Minion • Man
 * 10	3	4
 * Easterling. Fierce. Enduring.
 */
public class Card20_114 extends AbstractMinion {
    public Card20_114() {
        super(6, 10, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Detachment");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.ENDURING);
    }
}
