package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

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
        super(Side.SHADOW, Culture.SAURON, "Desperate Measures", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC))
                && game.getGameState().getBurdens() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addCost(new RemoveBurdenEffect());
        action.addEffect(
                new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
        action.addEffect(
                new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
        action.addEffect(
                new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
        action.addEffect(
                new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
        action.addEffect(
                new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
