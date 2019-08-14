package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 10
 * Type: Minion • Troll
 * Strength: 15
 * Vitality: 4
 * Site: 4
 * Game Text: Damage +1. Fierce. To play, spot a [MORIA] Orc. At an underground site, Cave Troll of Moria's twilight
 * cost is -3.
 */
public class Card1_165 extends AbstractMinion {
    public Card1_165() {
        super(10, 15, 4, 4, Race.TROLL, Culture.MORIA, "Cave Troll of Moria", "Scourge of the Black Pit", true);
        addKeyword(Keyword.DAMAGE);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.MORIA, Race.ORC);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        PhysicalCard currentSite = game.getGameState().getCurrentSite();
        if (game.getModifiersQuerying().hasKeyword(game, currentSite, Keyword.UNDERGROUND))
            return -3;
        return 0;
    }
}
