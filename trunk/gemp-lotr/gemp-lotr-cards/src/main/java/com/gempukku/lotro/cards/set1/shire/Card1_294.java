package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
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
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Hobbit Appetite", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new IntegerAwaitingDecision(1, "Choose how many twilight to add", 0) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                final int twilight = getValidatedResult(result);
                                action.addCost(new AddTwilightEffect(twilight));
                                action.addEffect(
                                        new ChooseActiveCardEffect(playerId, "Choose a Hobbit", Filters.keyword(Keyword.HOBBIT)) {
                                            @Override
                                            protected void cardSelected(PhysicalCard hobbit) {
                                                for (int i = 0; i < twilight; i++)
                                                    action.addEffect(new HealCharacterEffect(hobbit));
                                            }
                                        });
                            }
                        }));
        return action;
    }
}
