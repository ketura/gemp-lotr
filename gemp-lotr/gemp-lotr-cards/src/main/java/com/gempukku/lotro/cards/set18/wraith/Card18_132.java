package com.gempukku.lotro.cards.set18.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Event • Shadow
 * Game Text: To play, spot a Nazgul. Remove (X) to choose one: discard a follower from play that has a twilight
 * cost of X; or play from deck a [WRAITH] minion that has a twilight cost of X.
 */
public class Card18_132 extends AbstractEvent {
    public Card18_132() {
        super(Side.SHADOW, 2, Culture.WRAITH, "All Life Flees", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.appendCost(
                                new PlayoutDecisionEffect(playerId,
                                        new IntegerAwaitingDecision(1, "Choose X", 0, game.getGameState().getTwilightPool()) {
                                            @Override
                                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                                final int twilight = getValidatedResult(result);
                                                action.appendCost(
                                                        new RemoveTwilightEffect(twilight));
                                                List<Effect> possibleEffects = new LinkedList<Effect>();
                                                possibleEffects.add(
                                                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.FOLLOWER, Filters.printedTwilightCost(twilight)) {
                                                            @Override
                                                            public String getText(LotroGame game) {
                                                                return "Discard follower with twilight cost " + twilight;
                                                            }
                                                        });
                                                possibleEffects.add(
                                                        new ChooseAndPlayCardFromDeckEffect(playerId, Culture.WRAITH, CardType.MINION, Filters.printedTwilightCost(twilight)) {
                                                            @Override
                                                            public String getText(LotroGame game) {
                                                                return "Play WRAITH minion with twilight cost " + twilight + " from deck";
                                                            }
                                                        });
                                                action.appendEffect(
                                                        new ChoiceEffect(action, playerId, possibleEffects));
                                            }
                                        }));
                    }
                });
        return action;
    }
}
