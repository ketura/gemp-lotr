package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: To play, exert a [GANDALF] Wizard. Discard 2 cards from hand to take a Free Peoples card and a Shadow card
 * from your discard pile into hand.
 */
public class Card11_031 extends AbstractEvent {
    public Card11_031() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Final Account", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Culture.GANDALF, Race.WIZARD)
                && PlayConditions.canDiscardCardsFromHandToPlay(self, game, playerId, 2, Filters.any);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
        action.appendEffect(
                new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Side.FREE_PEOPLE));
        action.appendEffect(
                new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Side.SHADOW));
        return action;
    }
}
