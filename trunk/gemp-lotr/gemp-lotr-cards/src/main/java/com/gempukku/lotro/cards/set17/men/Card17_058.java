package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 14
 * Vitality: 3
 * Site: 4
 * Game Text: This minion is twilight cost -1 for each [MEN] minion stacked on a [MEN] possession.
 */
public class Card17_058 extends AbstractMinion {
    public Card17_058() {
        super(6, 14, 3, 4, Race.MAN, Culture.MEN, "Sunland Sneak");
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        int modifier = 0;
        for (PhysicalCard possession : Filters.filterActive(gameState, modifiersQuerying, Culture.MEN, CardType.POSSESSION, Filters.hasStacked(Culture.MEN, CardType.MINION))) {
            modifier -= Filters.filter(gameState.getStackedCards(possession), gameState, modifiersQuerying, Culture.MEN, CardType.MINION).size();
        }
        return modifier;
    }
}
