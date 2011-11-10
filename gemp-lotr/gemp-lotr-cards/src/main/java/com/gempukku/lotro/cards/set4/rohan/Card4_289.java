package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Spot 2 [ROHAN] Men (or 1 valiant [ROHAN] Man) to play a [ROHAN] character
 * or [ROHAN] possession from your draw deck.
 */
public class Card4_289 extends AbstractEvent {
    public Card4_289() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Simbelmyne", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && (
                PlayConditions.canSpot(game, 2, Culture.ROHAN, Race.MAN)
                        || PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN, Keyword.VALIANT));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId,
                        Filters.and(
                                Culture.ROHAN,
                                Filters.or(
                                        CardType.ALLY,
                                        CardType.COMPANION,
                                        CardType.POSSESSION
                                )
                        )));
        return action;
    }
}
