package com.gempukku.lotro.cards.set5.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 4
 * Vitality: 1
 * Site: 4
 * Game Text: Southron. Ambush (2). While you can spot a Southron, this minion's twilight cost is - 2.
 */
public class Card5_075 extends AbstractMinion {
    public Card5_075() {
        super(3, 4, 1, 4, Race.MAN, Culture.RAIDER, "Southron Runner");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 2);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Keyword.SOUTHRON))
            return -2;
        return 0;
    }
}
