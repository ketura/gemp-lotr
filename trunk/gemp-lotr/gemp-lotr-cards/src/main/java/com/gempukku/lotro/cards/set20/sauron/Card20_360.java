package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * ❸ •The Great Eye, Lidless [Sau]
 * Minion
 * Strength: 6   Vitality: 4   Roaming: 6
 * Cunning. (This minion may not take wounds during the archery phase and may not be assigned to a skirmish.)
 * When you play The Great Eye, add a threat for each companion over 4.
 * Each time the Ring-bearer puts on The One Ring, you may reveal this card from hand to add a burden.
 * Skirmish: Exert The Great Eye to make a [Sau] minion strength +2.
 * <p/>
 * http://lotrtcg.org/coreset/sauron/greateyel(r3).jpg
 */
public class Card20_360 extends AbstractMinion {
    public Card20_360() {
        super(3, 6, 4, 6, null, Culture.SAURON, "The Great Eye", "Lidless", true);
        addKeyword(Keyword.CUNNING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggersFromHand(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && !playerId.equals(game.getGameState().getCurrentPlayerId())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RevealCardEffect(self, self));
            action.appendEffect(
                    new AddBurdenEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(self.getOwner(), self,
                            new CountActiveEvaluator(4, (Integer) null, CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(
                            action, self, playerId, 2, Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
