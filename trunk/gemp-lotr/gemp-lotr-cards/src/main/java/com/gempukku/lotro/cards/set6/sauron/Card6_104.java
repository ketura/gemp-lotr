package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountCulturesEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 6
 * Game Text: Shadow: Exert this minion to draw X cards and add (X), where X is the number of Free Peoples cultures
 * you spot over 2.
 */
public class Card6_104 extends AbstractMinion {
    public Card6_104() {
        super(4, 11, 3, 6, Race.ORC, Culture.SAURON, "Orc Insurgent", null, true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            int x = new CountCulturesEvaluator(2, 1, Side.FREE_PEOPLE).evaluateExpression(game.getGameState(), game.getModifiersQuerying(), null);
                            if (x > 0)
                                action.appendEffect(
                                        new PlayoutDecisionEffect(playerId,
                                                new IntegerAwaitingDecision(1, "Choose number of FP cultures you wish to spot, over 2", 0, x, x) {
                                                    @Override
                                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                                        int spotCount = getValidatedResult(result);
                                                        action.appendEffect(
                                                                new DrawCardsEffect(action, playerId, spotCount));
                                                        action.appendEffect(
                                                                new AddTwilightEffect(self, spotCount));
                                                    }
                                                }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
