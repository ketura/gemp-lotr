package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Dwarf to discard cards from the top of your draw deck until you choose to stop
 * (limit 5). Add (1) for each card discarded in this way. Take the last card discarded into hand.
 */
public class Card1_022 extends AbstractLotroCardBlueprint {
    public Card1_022() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Mithril Shaft", "1_22");
        addKeyword(Keyword.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.MANEUVER
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF), Filters.canExert())) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose Dwarf to exert", Filters.keyword(Keyword.DWARF), Filters.canExert()) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard dwarf) {
                            action.addCost(new ExertCharacterEffect(dwarf));
                        }
                    }
            );
            action.addEffect(new DiscardAndChooseToPutToHandEffect(action, playerId, null, 0));
            return Collections.<Action>singletonList(action);
        }

        return null;
    }

    private class DiscardAndChooseToPutToHandEffect extends UnrespondableEffect {
        private CostToEffectAction _action;
        private String _player;
        private int _count;
        private PhysicalCard _lastCard;

        private DiscardAndChooseToPutToHandEffect(CostToEffectAction action, String player, PhysicalCard lastCard, int count) {
            _action = action;
            _player = player;
            _lastCard = lastCard;
            _count = count;
        }

        @Override
        public void playEffect(LotroGame game) {
            final GameState gameState = game.getGameState();
            PhysicalCard card = gameState.removeTopDeckCard(_player);
            if (card != null) {
                gameState.addCardToZone(card, Zone.DISCARD);
                gameState.addTwilight(1);
                _lastCard = card;
            }
            if (card != null && _count < 5) {
                _action.addEffect(new PlayoutDecisionEffect(game.getUserFeedback(), _player,
                        new MultipleChoiceAwaitingDecision(1, "Do you want to put " + _lastCard.getBlueprint().getName() + " in your hand?", new String[]{"Yes", "No"}) {
                            @Override
                            protected void validDecisionMade(int index, String result) {
                                if (result.equals("Yes")) {
                                    gameState.removeCardFromZone(_lastCard);
                                    gameState.addCardToZone(_lastCard, Zone.HAND);
                                } else {
                                    _action.addEffect(new DiscardAndChooseToPutToHandEffect(_action, _player, _lastCard, _count + 1));
                                }
                            }
                        }));
            } else if (_lastCard != null) {
                gameState.removeCardFromZone(_lastCard);
                gameState.addCardToZone(_lastCard, Zone.HAND);
            }
        }
    }
}
