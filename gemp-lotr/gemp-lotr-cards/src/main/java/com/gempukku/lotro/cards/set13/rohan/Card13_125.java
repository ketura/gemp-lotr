package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: To play, spot 2 [ROHAN] Men. Unless you can spot a companion who has resistance 3 or less, discard
 * a possession from play.
 */
public class Card13_125 extends AbstractEvent {
    public Card13_125() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Ferthu Theoden Hal", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.ROHAN, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        if (!PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(3)))
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION));
        return action;
    }
}
