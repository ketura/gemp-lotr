package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. While you have initiative and can spot a Nazgul, Ulaire Toldea’s twilight cost is -6.
 */
public class Card7_219 extends AbstractMinion {
    public Card7_219() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Toldea", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (modifiersQuerying.hasInitiative(gameState) == Side.SHADOW && Filters.canSpot(gameState, modifiersQuerying, Race.NAZGUL))
            return -6;
        return 0;
    }
}
