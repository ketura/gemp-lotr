package com.gempukku.lotro.cards.set31.gundabad;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * •Ancestral Feuds [Mirkwood] (Gundabad)
 * Condition • Support Area
 * Twilight Cost 2
 * 'Each time the fellowship moves to site 5, discard each weapon borne by a [Dwarven] companion.
 * Skirmish: Remove 3 to make a [Dwarven] character strength -X (limit -3), where X is the number of [Elven]
 * allies you spot.'
 */
public class Card31_029 extends AbstractPermanent {
    public Card31_029() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GUNDABAD, "Ancestral Feuds", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, Filters.siteNumber(5))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);

            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self.getOwner(),
                            self, Filters.weapon, Filters.attachedTo(Culture.DWARVEN, CardType.COMPANION)));

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new NegativeEvaluator(new CardPhaseLimitEvaluator(game, self, Phase.SKIRMISH, 3,
                                    new CountActiveEvaluator(CardType.ALLY, Culture.ELVEN))), Filters.and(Culture.DWARVEN, Filters.character)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
