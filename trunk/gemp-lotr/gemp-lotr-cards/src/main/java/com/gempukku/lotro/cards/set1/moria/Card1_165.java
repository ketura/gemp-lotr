package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

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
        super(10, 15, 4, 4, Race.TROLL, Culture.MORIA, "Cave Troll of Moria", true);
        addKeyword(Keyword.DAMAGE);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Culture.MORIA, Race.ORC);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        PhysicalCard currentSite = gameState.getCurrentSite();
        if (modifiersQuerying.hasKeyword(gameState, currentSite, Keyword.UNDERGROUND))
            return -3;
        return 0;
    }
}
