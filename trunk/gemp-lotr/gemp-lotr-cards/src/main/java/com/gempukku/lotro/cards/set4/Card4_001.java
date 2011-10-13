package com.gempukku.lotro.cards.set4;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Type: The One Ring
 * Vitality: +2
 * Game Text: While wearing The One Ring, the Ring-bearer is strength +2, and each time he is about to take a wound in
 * a skirmish, add a burden instead. Skirmish: Add a burden to wear The One Ring until the regroup phase.
 */
public class Card4_001 extends AbstractAttachable {
    public Card4_001() {
        super(Side.RING, CardType.THE_ONE_RING, 0, null, null, "The One Ring", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.none();
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return !modifiersQuerying.hasFlagActive(ModifierFlag.RING_TEXT_INACTIVE) && gameState.isWearingRing();
                            }
                        }, 2));

        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && !game.getModifiersQuerying().hasFlagActive(ModifierFlag.RING_TEXT_INACTIVE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(self, 1));
            action.appendEffect(
                    new PutOnTheOneRingEffect());
            action.appendEffect(new AddUntilStartOfPhaseActionProxyEffect(
                    new AbstractActionProxy() {
                        @Override
                        public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResult) {
                            if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                                    && ((StartOfPhaseResult) effectResult).getPhase() == Phase.REGROUP) {
                                ActivateCardAction action = new ActivateCardAction(self);
                                action.appendEffect(new TakeOffTheOneRingEffect());
                                return Collections.singletonList(action);
                            }
                            return null;
                        }
                    }
                    , Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.WOUND
                && game.getGameState().isWearingRing()
                && !game.getModifiersQuerying().hasFlagActive(ModifierFlag.RING_TEXT_INACTIVE)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            if (woundEffect.getAffectedCardsMinusPrevented(game).contains(self.getAttachedTo())) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(new PreventCardEffect(woundEffect, self.getAttachedTo()));
                action.appendEffect(new AddBurdenEffect(self, 1));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
