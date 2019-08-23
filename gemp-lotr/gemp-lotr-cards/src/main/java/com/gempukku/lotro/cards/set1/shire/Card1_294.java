package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Add (X) to heal a Hobbit companion X times.
 */
public class Card1_294 extends AbstractEvent {
    public Card1_294() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Hobbit Appetite", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose how many twilight to add", 0) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                final int twilight = getValidatedResult(result);
                                action.appendCost(new AddTwilightEffect(self, twilight));
                                action.appendEffect(
                                        new ChooseAndHealCharactersEffect(action, playerId, 1, 1, twilight, CardType.COMPANION, Race.HOBBIT));
                            }
                        }));
        return action;
    }
}
