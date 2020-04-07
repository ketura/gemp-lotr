package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. This minion is twilight cost -1 for each companion who has resistance 2 or less.
 */
public class Card13_171 extends AbstractMinion {
    public Card13_171() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Invader");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return -Filters.countActive(game, CardType.COMPANION, Filters.maxResistance(2));
    }
}
