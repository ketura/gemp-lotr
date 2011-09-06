package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

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
public class Card1_055 extends AbstractLotroCardBlueprint {
    public Card1_055() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, Culture.ELVEN, "The Mirror of Galadriel", true);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self))
            actions.add(getPlayCardAction(playerId, game, self, 0));

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Galadriel"), Filters.canExert())
                && opponentsHavingAtLeast7Cards(game, playerId).length > 0) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.MANEUVER, "Exert Galadriel to look at 2 random cards, discard one");
            PhysicalCard galadriel = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Galadriel"));
            action.addCost(new ExertCharacterEffect(galadriel));
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose opponent with at least 7 cards in hand", opponentsHavingAtLeast7Cards(game, playerId)) {
                                @Override
                                protected void validDecisionMade(int index, final String chosenOpponent) {
                                    List<? extends PhysicalCard> hand = game.getGameState().getHand(chosenOpponent);
                                    List<PhysicalCard> randomCardsFromHand = GameUtils.getRandomCards(hand, 2);
                                    action.addEffect(
                                            new ChooseArbitraryCardsEffect(playerId, "Choose card to discard", randomCardsFromHand, 1, 1) {
                                                @Override
                                                protected void cardsSelected(List<PhysicalCard> selectedCards) {
                                                    action.addEffect(
                                                            new DiscardCardFromDeckEffect(chosenOpponent, selectedCards.get(0)));
                                                }
                                            });
                                }
                            }));

            actions.add(action);
        }
        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(6)), 1);
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
