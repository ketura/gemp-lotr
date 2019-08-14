package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Spot a [SAURON] Orc and remove a burden to make the Free Peoples player discard top 5 cards
 * from his draw deck.
 */
public class Card1_245 extends AbstractEvent {
    public Card1_245() {
        super(Side.SHADOW, 2, Culture.SAURON, "Desperate Measures", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.SAURON, Race.ORC)
                && game.getGameState().getBurdens() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(new RemoveBurdenEffect(playerId, self));
        action.appendEffect(
                new DiscardTopCardFromDeckEffect(self, game.getGameState().getCurrentPlayerId(), 5, true));
        return action;
    }
}
