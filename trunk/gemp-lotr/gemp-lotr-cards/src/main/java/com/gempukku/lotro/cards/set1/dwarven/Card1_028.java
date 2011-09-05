package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Fellowship: Spot a Dwarf to reveal the top 3 cards of your draw deck. Take all Free Peoples cards
 * revealed into hand and discard the rest.
 */
public class Card1_028 extends AbstractEvent {
    public Card1_028() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Wealth of Moria", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF));
    }

    @Override
    public Action getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new UnrespondableEffect() {
                    @Override
                    public void playEffect(LotroGame game) {
                        List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                        int cardCount = Math.min(deck.size(), 3);
                        List<? extends PhysicalCard> cards = new LinkedList<PhysicalCard>(deck.subList(0, cardCount));

                        for (PhysicalCard card : cards) {
                            game.getGameState().removeCardFromZone(card);
                            if (card.getBlueprint().getSide() == Side.FREE_PEOPLE)
                                game.getGameState().addCardToZone(card, Zone.HAND);
                            else
                                game.getGameState().addCardToZone(card, Zone.DISCARD);
                        }
                    }
                });
        return action;
    }
}
