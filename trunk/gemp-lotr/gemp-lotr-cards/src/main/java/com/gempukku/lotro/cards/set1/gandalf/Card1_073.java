package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.StackCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Plays to your support area. Fellowship: Stack a Free Peoples artifact (or possession) from hand on this
 * card, or play a card stacked here as if played from hand.
 */
public class Card1_073 extends AbstractLotroCardBlueprint {
    public Card1_073() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, Culture.GANDALF, "Gandalf's Cart", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && checkPlayRequirements(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && (
                game.getGameState().getStackedCards(self).size() > 0
                        || Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.side(Side.FREE_PEOPLE), Filters.or(Filters.type(CardType.ARTIFACT), Filters.type(CardType.POSSESSION))).size() > 0)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Stack a Free Peoples artifact (or possession) from hand on this card, or play a card stacked here as if played from hand.");

            List<Effect> possibleChoices = new LinkedList<Effect>();
            possibleChoices.add(
                    new ChooseCardsFromHandEffect(playerId, "Choose Free Peoples artifact or possession", 1, 1, Filters.side(Side.FREE_PEOPLE), Filters.or(Filters.type(CardType.ARTIFACT), Filters.type(CardType.POSSESSION))) {
                        @Override
                        public String getText() {
                            return "Stack a Free Peoples artifact or possession";
                        }

                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            action.addEffect(new StackCardFromHandEffect(selectedCards.get(0), self));
                        }
                    });
            possibleChoices.add(
                    new ChooseArbitraryCardsEffect(playerId, "Choose artifact or possession to play", game.getGameState().getStackedCards(self), Filters.playable(game), 1, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, 0));
                        }
                    });

            action.addCost(
                    new ChoiceEffect(action, playerId, possibleChoices, false));

            return Collections.singletonList(action);
        }
        return null;
    }
}
