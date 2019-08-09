package com.gempukku.lotro.cards.set13.wraith;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;

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
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.nertea, "Servant of the Shadow", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (Filters.canSpot(game, CardType.COMPANION, Filters.maxResistance(3)))
            return -2;
        return 0;
    }
}
