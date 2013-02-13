package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Dead Marshes
 * 4	3
 * Marsh.
 * Nazgul are enduring.
 */
public class Card20_434 extends AbstractSite {
    public Card20_434() {
        super("Dead Marshes", Block.SECOND_ED, 4, 3, null);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, Race.NAZGUL, Keyword.ENDURING);
    }
}
