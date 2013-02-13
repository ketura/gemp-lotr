package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * White Rocks
 * 4	3
 * Battleground.
 * The Shadow number of this site is +1 for each mounted companion.
 */
public class Card20_439 extends AbstractSite {
    public Card20_439() {
        super("White Rocks", Block.SECOND_ED, 4, 3, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION, Filters.mounted);
    }
}
