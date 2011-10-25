package com.gempukku.lotro.cards.set5.gollum;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Companion
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ring-bound. To play add a burden. Regroup: Exert Smeagol (or Gollum) twice to reveal the top 4 cards
 * of your draw deck. Wound a minion for each Shadow card revealed. Place those 4 cards beneath your draw deck in any order.
 */
public class Card5_028 extends AbstractCompanion {
    public Card5_028() {
        super(0, 3, 4, Culture.GOLLUM, null, Signet.FRODO, "Smeagol", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier);
        playCardAction.appendCost(
                new AddBurdenEffect(self, 1));
        return playCardAction;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, 2, Filters.or(Filters.name("Gollum"), Filters.name("Smeagol")))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.or(Filters.name("Gollum"), Filters.name("Smeagol"))));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 4) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            int shadowCards = Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Side.SHADOW).size();
                            SubAction subAction = new SubAction(action);
                            for (int i = 0; i < cards.size(); i++)
                                action.appendEffect(
                                        new PutCardsOnBottomInAnyOrderEffect(game, playerId, cards));
                            game.getActionsEnvironment().addActionToStack(subAction);
                            for (int i = 0; i < shadowCards; i++)
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    private class PutCardsOnBottomInAnyOrderEffect extends AbstractSuccessfulEffect {
        private LotroGame _game;
        private String _playerId;
        private List<PhysicalCard> _cards;

        public PutCardsOnBottomInAnyOrderEffect(LotroGame game, String playerId, List<PhysicalCard> cards) {
            _game = game;
            _playerId = playerId;
            _cards = cards;
        }

        @Override
        public String getText(LotroGame game) {
            return null;
        }

        @Override
        public Effect.Type getType() {
            return null;
        }

        @Override
        public Collection<? extends EffectResult> playEffect(final LotroGame game) {
            if (_cards.size() == 1) {
                final PhysicalCard card = _cards.get(0);
                game.getGameState().removeCardsFromZone(_playerId, Collections.singleton(card));
                game.getGameState().putCardOnBottomOfDeck(card);
            } else if (_cards.size() > 1) {
                game.getUserFeedback().sendAwaitingDecision(
                        _playerId, new ArbitraryCardsSelectionDecision(1, "Choose card to put on bottom of deck", _cards, 1, 1) {
                    @Override
                    public void decisionMade(String result) throws DecisionResultInvalidException {
                        final List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                        if (selectedCards.size() == 1) {
                            PhysicalCard card = selectedCards.iterator().next();
                            _cards.remove(card);
                            game.getGameState().removeCardsFromZone(_playerId, Collections.singleton(card));
                            game.getGameState().putCardOnBottomOfDeck(card);
                        }
                    }
                });
            }
            return null;
        }
    }
}
