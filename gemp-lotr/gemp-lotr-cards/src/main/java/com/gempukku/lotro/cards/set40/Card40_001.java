package com.gempukku.lotro.cards.set40;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Title: The One Ring, Doom of Free Peoples
 * Set: Second Edition
 * Side: Free
 * Culture: Ring
 * Twilight Cost: 0
 * Type: The One Ring
 * Strength: +1
 * Vitality: +1
 * Resistance: +1
 * Card Number: 1R1
 * Game Text: While wearing The One Ring, each time the Ring-bearer is about to take a wound, add a burden instead.
 * Maneuver: Exert bearer or discard a card at random from hand to wear The One Ring until the regroup phase.
 */
public class Card40_001 extends AbstractAttachable {
    public Card40_001() {
        super(null, CardType.THE_ONE_RING, 0, null, null, "The One Ring", "Doom of Free Peoples", true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.none;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(new ResistanceModifier(self, Filters.hasAttached(self), 1));
        return modifiers;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && (PlayConditions.canExert(self, game, Filters.hasAttached(self))
                || PlayConditions.canDiscardFromHand(game, self.getOwner(), 1, Filters.any))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharactersEffect(action, self, Filters.hasAttached(self)));
            possibleCosts.add(
                    new DiscardCardAtRandomFromHandEffect(self, self.getOwner(), false));

            ChoiceEffect choiceEffect = new ChoiceEffect(action, self.getOwner(), possibleCosts);
            action.appendCost(choiceEffect);
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
                && !game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.RING_TEXT_INACTIVE)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new NegateWoundEffect((WoundCharactersEffect) effect, self.getAttachedTo()));
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
