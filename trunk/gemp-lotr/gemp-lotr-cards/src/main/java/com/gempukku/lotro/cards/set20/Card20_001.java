package com.gempukku.lotro.cards.set20;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Strength: +1
 * Vitality: +1
 * Resistance: +1
 * While wearing The One Ring, each time the Ring-bearer is about to take a wound, add a burden instead.
 * Manuever: Exert bearer or discard a card at random from hand to wear The One Ring until the regroup phase.
 */
public class Card20_001 extends AbstractAttachable {
    public Card20_001() {
        super(null, CardType.THE_ONE_RING, 0, null, null, "The One Ring", "Doom of Free Peoples", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.none;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new KeywordModifier(self, Filters.hasAttached(self), Keyword.RING_BOUND));
        modifiers.add(new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(new ResistanceModifier(self, Filters.hasAttached(self), 1));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && !game.getGameState().isWearingRing()
                && (PlayConditions.canExert(self, game, self.getAttachedTo()) || PlayConditions.canDiscardFromHand(game, playerId, 1))
                && !game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.RING_TEXT_INACTIVE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            possibleCosts.add(
                    new DiscardCardAtRandomFromHandEffect(self, playerId, false));

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new PutOnTheOneRingEffect());
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.hasAttached(self))
                && game.getGameState().isWearingRing()
                && !game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.RING_TEXT_INACTIVE)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new NegateWoundEffect(woundEffect, self.getAttachedTo()));
            action.appendEffect(new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if ((TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP) || TriggerConditions.endOfPhase(game, effectResult, Phase.REGROUP))
                && game.getGameState().isWearingRing()) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new TakeOffTheOneRingEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
