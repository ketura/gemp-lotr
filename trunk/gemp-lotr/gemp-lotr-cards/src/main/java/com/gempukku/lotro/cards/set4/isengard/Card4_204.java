package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. The twilight cost of this minion is -1 for each site you control.
 */
public class Card4_204 extends AbstractMinion {
    public Card4_204() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk-hai Marauder");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        twilightModifier -= Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId));
        return super.checkPlayRequirements(playerId, game, self, twilightModifier);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        twilightModifier -= Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId));
        return super.getPlayCardAction(playerId, game, self, twilightModifier);
    }
}
