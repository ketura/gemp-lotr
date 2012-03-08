package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: If the fellowship is at a plains site, exert a [ROHAN] companion to return a minion to its owner's hand.
 */
public class Card18_098 extends AbstractEvent {
    public Card18_098() {
        super(Side.FREE_PEOPLE, 3, Culture.ROHAN, "Fall Back to Helm's Deep", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.location(game, Keyword.PLAINS)
                && PlayConditions.canExert(self, game, Culture.ROHAN, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, CardType.COMPANION));
        action.appendEffect(
                new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, CardType.MINION));
        return action;
    }
}
