package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountStackedEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Throne of Orthanc
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Artifact - Support Area
 * Card Number: 1R142
 * Game Text: The twilight cost of each [ISENGARD] event is -1 for each minion stacked on this artifact.
 * Regroup: If Saruman is not exhausted, stack him on this artifact.
 * Shadow: Remove a threat to play Saruman stacked here as if from hand.
 */
public class Card40_142 extends AbstractPermanent {
    public Card40_142() {
        super(Side.SHADOW, 2, CardType.ARTIFACT, Culture.ISENGARD, Zone.SUPPORT, "Throne of Orthanc", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        TwilightCostModifier modifier = new TwilightCostModifier(self, Filters.and(Culture.ISENGARD, CardType.EVENT), null,
                new NegativeEvaluator(new CountStackedEvaluator(self, CardType.MINION)));
        return Collections.singletonList(modifier);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
        && PlayConditions.canRemoveThreat(game, self, 1)
        && PlayConditions.canPlayFromStacked(playerId, game, self, Filters.saruman)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Filters.saruman));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 1)
                && PlayConditions.canSpot(game, Filters.saruman, Filters.not(Filters.exhausted))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardFromPlayEffect(action, playerId, self, Filters.saruman, Filters.not(Filters.exhausted)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
