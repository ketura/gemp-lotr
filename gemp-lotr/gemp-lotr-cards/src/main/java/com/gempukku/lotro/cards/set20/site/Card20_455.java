package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Harad Road
 * 7	7
 * Southrons are ambush (1).
 * http://lotrtcg.org/coreset/sites/7haradroad(r1).png
 */
public class Card20_455 extends AbstractSite {
    public Card20_455() {
        super("Harad Road", SitesBlock.SECOND_ED, 7, 7, null);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Keyword.SOUTHRON, Keyword.AMBUSH, 1));
}
}
