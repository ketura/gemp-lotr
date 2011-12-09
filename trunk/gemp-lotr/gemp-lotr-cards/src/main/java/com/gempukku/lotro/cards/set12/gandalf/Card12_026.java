package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.ReorderTopCardsOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 7
 * Type: Event • Fellowship
 * Game Text: Toil 3. (For each [GANDALF] character you exert when playing this, its twilight cost is -3)
 * Spot a [GANDALF] wizard and X other companions to examine the top X cards of your draw deck. Replace those cards
 * in any order.
 */
public class Card12_026 extends AbstractEvent {
    public Card12_026() {
        super(Side.FREE_PEOPLE, 7, Culture.GANDALF, "Discoveries", Phase.FELLOWSHIP);
        addKeyword(Keyword.TOIL, 3);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, CardType.COMPANION, Filters.not(Culture.GANDALF, Race.WIZARD)) {
                    @Override
                    protected void spottedCards(int spotCount) {
                        action.appendEffect(
                                new ReorderTopCardsOfDeckEffect(action, playerId, spotCount));
                    }
                });
        return action;
    }
}
