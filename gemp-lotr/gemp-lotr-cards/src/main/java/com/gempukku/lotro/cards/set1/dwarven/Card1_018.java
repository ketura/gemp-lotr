package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
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
public class Card1_018 extends AbstractOldEvent {
    public Card1_018() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Halls of My Home", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF));
        action.appendEffect(
                new PlayoutDecisionEffect(playerId,
                        new MultipleChoiceAwaitingDecision(1, "Choose player", GameUtils.getAllPlayers(game)) {
                            @Override
                            protected void validDecisionMade(int index, final String chosenPlayerId) {
                                action.insertEffect(
                                        new RevealTopCardsOfDrawDeckEffect(self, chosenPlayerId, 3) {
                                            @Override
                                            protected void cardsRevealed(final List<PhysicalCard> topDeckCards) {
                                                if (topDeckCards.size() > 0) {
                                                    Collection<PhysicalCard> shadowCards = Filters.filter(topDeckCards, game.getGameState(), game.getModifiersQuerying(), Side.SHADOW);

                                                    if (shadowCards.size() > 0) {
                                                        action.appendEffect(
                                                                new ChooseArbitraryCardsEffect(playerId, "Choose shadow card to discard", new LinkedList<PhysicalCard>(shadowCards), 0, 1) {
                                                                    @Override
                                                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                                        if (selectedCards.size() > 0) {
                                                                            action.appendEffect(new DiscardCardFromDeckEffect(selectedCards.iterator().next()));
                                                                            topDeckCards.removeAll(selectedCards);
                                                                        }

                                                                        if (topDeckCards.size() > 0)
                                                                            game.getGameState().removeCardsFromZone(playerId, topDeckCards);
                                                                    }
                                                                });
                                                    }

                                                    action.appendEffect(new ChooseCardToPutOnTop(action, playerId, topDeckCards));
                                                }
                                            }
                                        });
                            }
                        }));
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
        public void doPlayEffect(final LotroGame game) {
            _action.appendEffect(
                    new ChooseArbitraryCardsEffect(_playerIdDeciding, "Choose card to put on top", _cardsToPutOnTop, 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard card = selectedCards.iterator().next();
                                game.getGameState().putCardOnTopOfDeck(card);
                                _cardsToPutOnTop.remove(card);
                                if (_cardsToPutOnTop.size() > 0)
                                    _action.appendEffect(new ChooseCardToPutOnTop(_action, _playerIdDeciding, _cardsToPutOnTop));
                            }
                        }
                    }
            );
        }
    }
}
