package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Sauron's Forces
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Condition - Support Area
 * Card Number: 1U238
 * Game Text: To play, exert a [SAURON] minion.
 * Shadow: Discard this condition to add (1) for each threat.
 * Maneuver: Spot a [SAURON] minion, 6 companions, and remove (2) (or 2 threats) to wound a companion (except the Ring-bearer.)
 */
public class Card40_238 extends AbstractPermanent {
    public Card40_238() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.SAURON, "Sauron's Forces", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.SAURON, CardType.MINION));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new AddTwilightEffect(self, new ForEachThreatEvaluator()));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION)
                && PlayConditions.canSpot(game, 6, CardType.COMPANION)
                && (game.getGameState().getTwilightPool() >= 2 || PlayConditions.canRemoveThreat(game, self, 2))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new ArrayList<>(2);
            possibleCosts.add(
                    new RemoveTwilightEffect(2));
            possibleCosts.add(
                    new RemoveThreatsEffect(self, 2));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.ringBearer)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
