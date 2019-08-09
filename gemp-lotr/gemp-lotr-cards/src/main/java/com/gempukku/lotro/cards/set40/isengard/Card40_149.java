package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Title: Uruk Hunting Party
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 5
 * Type: Minion - Uruk-hai
 * Strength: 10
 * Vitality: 3
 * Home: 5
 * Card Number: 1U149
 * Game Text: Damage +1. If the fellowship has moved more than once this turn, this minion's twilight cost is -2.
 */
public class Card40_149 extends AbstractMinion {
    public Card40_149() {
        super(5, 10, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Hunting Party");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getMoveCount() > 1)
            return -2;
        return 0;
    }
}
