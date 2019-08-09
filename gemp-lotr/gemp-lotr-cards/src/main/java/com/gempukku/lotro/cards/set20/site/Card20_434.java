package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Dead Marshes
 * 4	3
 * Marsh.
 * Twilight minions are enduring.
 * http://www.lotrtcg.org/coreset/sites/4deadmarshes(r2).jpg
 */
public class Card20_434 extends AbstractSite {
    public Card20_434() {
        super("Dead Marshes", SitesBlock.SECOND_ED, 4, 3, null);
        addKeyword(Keyword.MARSH);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(CardType.MINION, Keyword.TWILIGHT), Keyword.ENDURING);
    }
}
