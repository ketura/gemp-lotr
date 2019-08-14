package com.gempukku.lotro.cards.set15.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TransferToSupportEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Transfer a follower from a Hobbit to your support area to cancel a skirmish involving that Hobbit.
 */
public class Card15_150 extends AbstractEvent {
    public Card15_150() {
        super(Side.FREE_PEOPLE, 2, Culture.SHIRE, "No Visitors", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, CardType.FOLLOWER, Filters.attachedTo(Race.HOBBIT));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose follower", CardType.FOLLOWER, Filters.attachedTo(Race.HOBBIT)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendCost(
                                new TransferToSupportEffect(card));
                        action.appendEffect(
                                new CancelSkirmishEffect(card.getAttachedTo()));
                    }
                });
        return action;
    }
}
