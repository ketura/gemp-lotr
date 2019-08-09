package com.gempukku.lotro.cards.set9.sauron;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 18
 * Type: Minion • Maia
 * Strength: 24
 * Vitality: 5
 * Site: 6
 * Game Text: Damage +2. Enduring. Fierce. For each burden you spot, threat you spot, and site you control, Sauron's
 * twilight cost is -1.
 */
public class Card9_048 extends AbstractMinion {
    public Card9_048() {
        super(18, 24, 5, 6, Race.MAIA, Culture.SAURON, "Sauron", "The Lord of the Rings", true);
        addKeyword(Keyword.DAMAGE, 2);
        addKeyword(Keyword.ENDURING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return -(game.getGameState().getBurdens() + game.getGameState().getThreats() + Filters.countActive(game, Filters.siteControlled(self.getOwner())));
    }
}
