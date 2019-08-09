package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: While you can spot a companion who has resistance 3 or less, this minion's twilight cost is -2.
 */
public class Card11_069 extends AbstractMinion {
    public Card11_069() {
        super(4, 11, 3, 4, Race.MAN, Culture.MEN, "Axeman of Harad");
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (Filters.canSpot(game, CardType.COMPANION, Filters.maxResistance(3)))
            return -2;
        return 0;
    }
}
