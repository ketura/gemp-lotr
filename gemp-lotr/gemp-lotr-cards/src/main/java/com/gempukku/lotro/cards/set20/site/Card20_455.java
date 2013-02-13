package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Harad Road
 * 7	7
 * Southrons are ambush (2).
 */
public class Card20_455 extends AbstractSite {
    public Card20_455() {
        super("Harad Road", Block.SECOND_ED, 7, 7, null);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, Keyword.SOUTHRON, Keyword.AMBUSH, 2);
    }
}
