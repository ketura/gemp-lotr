package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
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
public class Card1_028 extends AbstractLotroCardBlueprint {
    public Card1_028() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Wealth of Moria", "1_28");
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF))) {
            PlayEventAction action = new PlayEventAction(self);
            action.addEffect(
                    new UnrespondableEffect() {
                        @Override
                        public void playEffect(LotroGame game) {
                            List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                            int cardCount = Math.min(deck.size(), 3);
                            List<? extends PhysicalCard> cards = deck.subList(0, cardCount);

                            for (PhysicalCard card : cards) {
                                game.getGameState().removeCardFromZone(card);
                                if (card.getBlueprint().getSide() == Side.FREE_PEOPLE)
                                    game.getGameState().addCardToZone(card, Zone.HAND);
                                else
                                    game.getGameState().addCardToZone(card, Zone.DISCARD);
                            }
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
