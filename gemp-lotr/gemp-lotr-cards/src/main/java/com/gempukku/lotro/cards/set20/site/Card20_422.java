package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Barrow-downs
 * 2	2
 * While the Shadow player has initaitive, this site's shadow number is +3.
 */
public class Card20_422 extends AbstractSite {
    public Card20_422() {
        super("Barrow-downs", Block.SECOND_ED, 2, 2, null);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return (modifiersQuerying.hasInitiative(gameState) == Side.SHADOW) ? 3 : 0;
    }
}
