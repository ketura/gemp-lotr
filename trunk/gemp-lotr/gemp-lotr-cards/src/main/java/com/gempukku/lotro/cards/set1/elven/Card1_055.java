package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession
 * Game Text: Plays to your support area. Each Elf ally whose home is site 6 is strength +1. Maneuver: If an opponent
 * has at least 7 cards in hand, exert Galadriel to look at 2 of those cards at random. Discard one and replace the other.
 */
public class Card1_055 extends AbstractPermanent {
    public Card1_055() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.ELVEN, Zone.SUPPORT, "The Mirror of Galadriel", true);
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.name("Galadriel"))
                && opponentsHavingAtLeast7Cards(game, playerId).length > 0) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            PhysicalCard galadriel = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Galadriel"));
            action.appendCost(new ExertCharactersEffect(self, galadriel));
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose opponent with at least 7 cards in hand", opponentsHavingAtLeast7Cards(game, playerId)) {
                                @Override
                                protected void validDecisionMade(int index, final String chosenOpponent) {
                                    if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), chosenOpponent)) {
                                        List<? extends PhysicalCard> hand = game.getGameState().getHand(chosenOpponent);
                                        List<PhysicalCard> randomCardsFromHand = GameUtils.getRandomCards(hand, 2);
                                        action.appendEffect(
                                                new ChooseArbitraryCardsEffect(playerId, "Choose card to discard", randomCardsFromHand, 1, 1) {
                                                    @Override
                                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                        for (PhysicalCard selectedCard : selectedCards)
                                                            action.appendEffect(
                                                                    new DiscardCardFromDeckEffect(chosenOpponent, selectedCard));
                                                    }
                                                });
                                    }
                                }
                            }));

            actions.add(action);
        }
        return actions;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(6), Filters.siteBlock(Block.FELLOWSHIP)), 1);
    }

    private String[] opponentsHavingAtLeast7Cards(LotroGame game, String currentPlayer) {
        String[] opponents = GameUtils.getOpponents(game, currentPlayer);
        List<String> result = new LinkedList<String>();

        for (String shadowPlayer : opponents)
            if (game.getGameState().getHand(shadowPlayer).size() > 6)
                result.add(shadowPlayer);

        return result.toArray(new String[result.size()]);
    }
}
