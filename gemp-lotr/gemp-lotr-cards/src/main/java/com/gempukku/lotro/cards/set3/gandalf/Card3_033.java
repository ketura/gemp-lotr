package com.gempukku.lotro.cards.set3.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Spot Gandalf to reveal a card at random from an opponent's hand. You may add (X) to discard
 * that card, where X is the twilight cost of the card revealed.
 */
public class Card3_033 extends AbstractOldEvent {
    public Card3_033() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "His First Serious Check", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.gandalf);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(final String opponentId) {
                        action.insertEffect(
                                new RevealRandomCardsFromHandEffect(playerId, opponentId, self, 1) {
                                    @Override
                                    protected void cardsRevealed(final List<PhysicalCard> revealedCards) {
                                        if (revealedCards.size() > 0) {
                                            final PhysicalCard revealedCard = revealedCards.iterator().next();
                                            action.insertEffect(
                                                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                                            new MultipleChoiceAwaitingDecision(1, "Do you wish to discard " + revealedCard.getBlueprint().getName(), new String[]{"Yes", "No"}) {
                                                                @Override
                                                                protected void validDecisionMade(int index, String result) {
                                                                    if (result.equals("Yes")) {
                                                                        action.appendEffect(
                                                                                new AddTwilightEffect(self, revealedCard.getBlueprint().getTwilightCost()));
                                                                        action.appendEffect(
                                                                                new DiscardCardsFromHandEffect(self, opponentId, revealedCards, true));
                                                                    }
                                                                }
                                                            }));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
