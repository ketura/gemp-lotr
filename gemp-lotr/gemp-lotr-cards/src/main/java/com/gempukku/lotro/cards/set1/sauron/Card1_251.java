package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Search. Maneuver: Spot a [SAURON] Orc and 6 companions to wound a companion (except the Ring-bearer).
 * Do this once for each companion over 5.
 */
public class Card1_251 extends AbstractOldEvent {
    public Card1_251() {
        super(Side.SHADOW, Culture.SAURON, "A Host Avails Little", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Culture.SAURON, Race.ORC)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) >= 6;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        int companionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
        int woundCount = companionCount - 5;
        for (int i = 0; i < woundCount; i++) {
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.keyword(Keyword.RING_BEARER))));
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }
}
