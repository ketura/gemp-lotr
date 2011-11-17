package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 6
 * Game Text: Damage +1. If you can spot another [SAURON] Uruk-hai, this minion is twilight cost -1 for each possession
 * you can spot.
 */
public class Card10_083 extends AbstractMinion {
    public Card10_083() {
        super(2, 7, 2, 6, Race.URUK_HAI, Culture.SAURON, "Cirith Ungol Sentinel");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Filters.not(self), Culture.SAURON, Race.URUK_HAI))
            return -Filters.countSpottable(gameState, modifiersQuerying, CardType.POSSESSION);
        return 0;
    }
}
