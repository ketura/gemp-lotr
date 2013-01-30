package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * 5
 * Uruk Reserves
 * Isengard	Minion • Uruk-hai
 * 10	3	5
 * Damage +1.
 * This minion is twilight cost - 1 for each battleground in the current region.
 */
public class Card20_225 extends AbstractMinion {
    public Card20_225() {
        super(5, 10, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Reserves");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return Filters.countActive(gameState, modifiersQuerying, CardType.SITE, Keyword.BATTLEGROUND, Filters.region(GameUtils.getRegion(gameState)));
    }
}
