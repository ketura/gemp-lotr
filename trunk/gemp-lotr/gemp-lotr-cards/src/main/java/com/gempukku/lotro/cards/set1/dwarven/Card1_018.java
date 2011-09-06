package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Exert a Dwarf to reveal the top 3 cards of any draw deck. You may discard 1 Shadow card
 * revealed. Return the rest in any order.
 */
public class Card1_018 extends AbstractEvent {
    public Card1_018() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Halls of My Home", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a Dwarf", true, Filters.keyword(Keyword.DWARF), Filters.canExert()));
        action.addEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new MultipleChoiceAwaitingDecision(1, "Choose player", GameUtils.getAllPlayers(game)) {
                            @Override
                            protected void validDecisionMade(int index, final String chosenPlayerId) {
                                List<? extends PhysicalCard> deck = game.getGameState().getDeck(chosenPlayerId);
                                int topCards = Math.min(deck.size(), 3);
                                final List<PhysicalCard> topDeckCards = new LinkedList<PhysicalCard>(deck.subList(0, topCards));

                                if (topDeckCards.size() > 0) {
                                    List<PhysicalCard> shadowCards = Filters.filter(topDeckCards, game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.SHADOW));

                                    if (shadowCards.size() > 0) {
                                        action.addEffect(
                                                new ChooseArbitraryCardsEffect(playerId, "Choose shadow card to discard", shadowCards, 0, 1) {
                                                    @Override
                                                    protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                                        if (selectedCards.size() > 0) {
                                                            action.addEffect(new DiscardCardFromDeckEffect(chosenPlayerId, selectedCards.get(0)));
                                                            topDeckCards.remove(selectedCards.get(0));
                                                        }

                                                        if (topDeckCards.size() > 0) {
                                                            for (PhysicalCard topDeckCard : topDeckCards)
                                                                game.getGameState().removeCardFromZone(topDeckCard);

                                                        }
                                                    }
                                                });
                                    }

                                    action.addEffect(new ChooseCardToPutOnTop(action, playerId, topDeckCards));
                                }
                            }
                        })
        );
        return action;
    }

    private class ChooseCardToPutOnTop extends UnrespondableEffect {
        private CostToEffectAction _action;
        private String _playerIdDeciding;
        private List<PhysicalCard> _cardsToPutOnTop;

        private ChooseCardToPutOnTop(CostToEffectAction action, String playerIdDeciding, List<PhysicalCard> cardsToPutOnTop) {
            _action = action;
            _playerIdDeciding = playerIdDeciding;
            _cardsToPutOnTop = cardsToPutOnTop;
        }

        @Override
        public void playEffect(final LotroGame game) {
            _action.addEffect(
                    new ChooseArbitraryCardsEffect(_playerIdDeciding, "Choose card to put on top", _cardsToPutOnTop, 1, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard card = selectedCards.get(0);
                            game.getGameState().putCardOnTopOfDeck(card);
                            _cardsToPutOnTop.remove(card);
                            if (_cardsToPutOnTop.size() > 0)
                                _action.addEffect(new ChooseCardToPutOnTop(_action, _playerIdDeciding, _cardsToPutOnTop));
                        }
                    }
            );
        }
    }
}
