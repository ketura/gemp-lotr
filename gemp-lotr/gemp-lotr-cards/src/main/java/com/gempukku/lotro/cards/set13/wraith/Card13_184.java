package com.gempukku.lotro.cards.set13.wraith;

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
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Fierce. While you can spot a companion who has resistance 3 or less, Ulaire Nertea is twilight cost -2.
 */
public class Card13_184 extends AbstractMinion {
    public Card13_184() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Nertëa", "Servant of the Shadow", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, CardType.COMPANION, Filters.maxResistance(3)))
            return -2;
        return 0;
    }
}
