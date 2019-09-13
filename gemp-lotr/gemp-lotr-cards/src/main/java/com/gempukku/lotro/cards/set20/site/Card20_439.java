package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;

/**
 * White Rocks
 * 4	3
 * Battleground.
 * The Shadow number of this site is +1 for each mounted companion.
 */
public class Card20_439 extends AbstractSite {
    public Card20_439() {
        super("White Rocks", SitesBlock.SECOND_ED, 4, 3, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return Filters.countActive(game, CardType.COMPANION, Filters.mounted);
    }
}
