package com.gempukku.lotro.cards.set5.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Spot Smeagol or Gollum to add (1) for each burden.
 */
public class Card5_026 extends AbstractEvent {
    public Card5_026() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "Look at Him", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Filters.or(Filters.name("Smeagol"), Filters.name("Gollum")));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddTwilightEffect(self, game.getGameState().getBurdens()));
        return action;
    }
}
