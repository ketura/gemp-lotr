package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Title: Barrow-downs
 * Set: Second Edition
 * Side: None
 * Site Number: 2
 * Shadow Number: 2
 * Card Number: 1U279
 * Game Text: While the Shadow player has initiative, this site's shadow number is +3.
 */
public class Card40_279 extends AbstractSite {
    public Card40_279() {
        super("Barrow-downs", Block.SECOND_ED, 2, 2, Direction.LEFT);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (modifiersQuerying.hasInitiative(gameState) == Side.SHADOW)
            return 3;
        return 0;
    }
}
