package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.RevealCardsFromYourHandEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MinEvaluator;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *The Great Eye, Wreathed in Flame
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion
 * Strength: 6
 * Vitality: 4
 * Home: 6
 * Card Number: 1R219
 * Game Text: Cunning.
 * When you play The Great Eye, add a threat for each companion over 4.
 * Each time the Ring-bearer puts on The One Ring, you may reveal this card from hand to add a burden.
 * Skirmish: Exert The Great Eye to make a [SAURON] minion strength +2.
 */
public class Card40_219 extends AbstractMinion {
    public Card40_219() {
        super(3, 6, 4, 6, null, Culture.SAURON, "The Great Eye", "Wreathed in Flame", true);
        addKeyword(Keyword.CUNNING);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(self.getOwner(), self,
                            new MinEvaluator(new CountSpottableEvaluator(4, (Integer) null, CardType.COMPANION), 0)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalInHandAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RevealCardsFromYourHandEffect(self, playerId, self));
            action.appendEffect(
                    new AddBurdenEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
