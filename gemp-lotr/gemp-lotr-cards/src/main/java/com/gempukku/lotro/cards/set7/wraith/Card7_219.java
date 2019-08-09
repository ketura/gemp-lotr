package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Minion • Nazgul
 * Strength: 12
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. While you have initiative and can spot a Nazgul, Úlairë Toldëa’s twilight cost is -6.
 */
public class Card7_219 extends AbstractMinion {
    public Card7_219() {
        super(6, 12, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.toldea, "Wraith on Wings", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (game.getModifiersQuerying().hasInitiative(game) == Side.SHADOW && Filters.canSpot(game, Race.NAZGUL))
            return -6;
        return 0;
    }
}
