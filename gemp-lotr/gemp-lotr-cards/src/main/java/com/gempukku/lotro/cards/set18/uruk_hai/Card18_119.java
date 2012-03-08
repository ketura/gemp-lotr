package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 1
 * Type: Possession • Ranged Weapon
 * Strength: +1
 * Vitality: +1
 * Game Text: Bearer must be an Uruk-hai. When you play this possession on Lurtz, you may draw a card.
 * Archery: If bearer is Lurtz, discard 2 [URUK-HAI] cards from hand to exert an unbound companion.
 */
public class Card18_119 extends AbstractAttachable {
    public Card18_119() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.URUK_HAI, PossessionClass.RANGED_WEAPON, "Lurtz's Bow", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        return modifiers;
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.URUK_HAI;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedOn(game, effectResult, Filters.name("Lurtz"), self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && PlayConditions.canSpot(game, Filters.name("Lurtz"), Filters.hasAttached(self))
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Culture.URUK_HAI)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2, Culture.URUK_HAI));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
