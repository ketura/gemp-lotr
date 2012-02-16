package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: This minion is strength +2 for each [MEN] possession with a [MEN] minion stacked on it. Maneuver: Spot
 * X [MEN] minions stacked on a [MEN] possession and discard this minion to add (X).
 */
public class Card17_059 extends AbstractMinion {
    public Card17_059() {
        super(3, 9, 2, 4, Race.MAN, Culture.MEN, "Sunland Trooper");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new MultiplyEvaluator(2,
                        new CountActiveEvaluator(Culture.MEN, CardType.POSSESSION, Filters.hasStacked(Culture.MEN, CardType.MINION))));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            int count = 0;
            for (PhysicalCard possession : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Culture.MEN, CardType.POSSESSION, Filters.hasStacked(Culture.MEN, CardType.MINION))) {
                count += Filters.filter(game.getGameState().getStackedCards(possession), game.getGameState(), game.getModifiersQuerying(), Culture.MEN, CardType.MINION).size();
            }
            action.appendCost(
                    new PlayoutDecisionEffect(playerId,
                            new IntegerAwaitingDecision(1, "How many minions you wish to spot?", count, count, count) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotted = getValidatedResult(result);
                                    action.appendEffect(
                                            new AddTwilightEffect(self, spotted));
                                }
                            }));
            action.appendCost(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
