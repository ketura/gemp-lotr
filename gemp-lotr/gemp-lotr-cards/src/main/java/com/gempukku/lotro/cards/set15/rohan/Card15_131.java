package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Spot a [ROHAN] Man to play a possession from your discard pile for each hunter character you can spot.
 */
public class Card15_131 extends AbstractEvent {
    public Card15_131() {
        super(Side.FREE_PEOPLE, 2, Culture.ROHAN, "Our Inspiration", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN)
                && PlayConditions.canPlayFromDiscard(self.getOwner(), game, CardType.POSSESSION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        int huntersCount = Filters.countActive(game, Filters.character, Keyword.HUNTER);
        for (int i = 0; i < huntersCount; i++)
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.POSSESSION));
        return action;
    }
}
