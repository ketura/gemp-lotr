package com.gempukku.lotro.cards.set13.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Discard 2 Shadow cards from hand to take a [GANDALF] event into hand from your discard pile.
 */
public class Card13_039 extends AbstractEvent {
    public Card13_039() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Return to Us", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardCardsFromHandToPlay(self, game, self.getOwner(), 2, Side.SHADOW);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Side.SHADOW));
        action.appendEffect(
                new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.GANDALF, CardType.EVENT));
        return action;
    }
}
