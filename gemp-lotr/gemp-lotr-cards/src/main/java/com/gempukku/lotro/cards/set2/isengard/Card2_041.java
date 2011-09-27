package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.effects.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.Collection;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Exert an Uruk-hai and spot X burdens to shuffle X minions from your discard pile into your draw
 * deck.
 */
public class Card2_041 extends AbstractEvent {
    public Card2_041() {
        super(Side.SHADOW, Culture.ISENGARD, "Evil Afoot", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI), Filters.canExert());
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.race(Race.URUK_HAI)));
        action.appendCost(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new IntegerAwaitingDecision(1, "Choose number of burdens to spot", 0, game.getGameState().getBurdens()) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int numberOfSpotted = getValidatedResult(result);
                                action.appendEffect(
                                        new ChooseCardsFromDiscardEffect(playerId, numberOfSpotted, numberOfSpotted, Filters.type(CardType.MINION)) {
                                            @Override
                                            protected void cardsSelected(Collection<PhysicalCard> cards) {
                                                for (PhysicalCard card : cards) {
                                                    action.appendEffect(
                                                            new PutCardFromDiscardOnBottomOfDeckEffect(card));
                                                }
                                                action.appendEffect(
                                                        new ShuffleDeckEffect(playerId));
                                            }
                                        });
                            }
                        }));
        return action;
    }
}
