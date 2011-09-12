package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Exert a Dwarf to discard cards from the top of your draw deck until you choose to stop
 * (limit 5). Add (1) for each card discarded in this way. Take the last card discarded into hand.
 */
public class Card1_022 extends AbstractEvent {
    public Card1_022() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Mithril Shaft", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose Dwarf to exert", true, Filters.race(Race.DWARF), Filters.canExert()));
        action.addEffect(new DiscardAndChooseToPutToHandEffect(action, playerId, null, 0));
        return action;
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
        public void doPlayEffect(LotroGame game) {
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
                                    _action.addEffect(new PutCardFromDiscardIntoHandEffect(_lastCard));
                                } else {
                                    _action.addEffect(new DiscardAndChooseToPutToHandEffect(_action, _player, _lastCard, _count + 1));
                                }
                            }
                        }));
            } else if (_lastCard != null) {
                _action.addEffect(new PutCardFromDiscardIntoHandEffect(_lastCard));
            }
        }
    }
}
