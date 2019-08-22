package com.gempukku.lotro.cards.set32.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Spot an Orc to play a [Sauron] Orc (or 2 [Sauron] Orcs if you spot a 
 * character in the dead pile) from your discard pile.
 */
public class Card32_038 extends AbstractEvent {
    public Card32_038() {
        super(Side.SHADOW, 0, Culture.SAURON, "Hidden Attack", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ORC)
                && PlayConditions.canPlayFromDiscard(self.getOwner(), game, Culture.SAURON, Race.ORC);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.and(Culture.SAURON, Race.ORC)));
        if (Filters.filter(game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()), game, CardType.COMPANION).size() > 0) {
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.and(Culture.SAURON, Race.ORC)));
        }
        return action;
    }
}
