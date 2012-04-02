package com.gempukku.lotro.cards.set0.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Promotional
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Damage +1. Valiant. While you can spot a [ROHAN] Man, Eomer's twilight cost is -1.
 */
public class Card0_066 extends AbstractCompanion {
    public Card0_066() {
        super(2, 7, 3, 6, Culture.ROHAN, Race.MAN, null, Names.eomer, "Forthwith Banished", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Culture.ROHAN, Race.MAN))
            return -1;
        return 0;
    }
}
