package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

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
        super(10, 15, 4, 4, Keyword.TROLL, Culture.MORIA, "Cave Troll of Moria", true);
        addKeyword(Keyword.DAMAGE);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PhysicalCard currentSite = game.getGameState().getCurrentSite();
        if (game.getModifiersQuerying().hasKeyword(game.getGameState(), currentSite, Keyword.UNDERGROUND))
            twilightModifier -= 3;
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC));
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PhysicalCard currentSite = game.getGameState().getCurrentSite();
        if (game.getModifiersQuerying().hasKeyword(game.getGameState(), currentSite, Keyword.UNDERGROUND))
            twilightModifier -= 3;
        return super.getPlayCardAction(playerId, game, self, twilightModifier);
    }
}
