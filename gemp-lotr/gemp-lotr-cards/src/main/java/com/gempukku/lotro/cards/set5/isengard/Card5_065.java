package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.CancelActivatedEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Possession • Mount
 * Strength: +3
 * Vitality: +1
 * Game Text: Bearer must a warg-rider. Response: If a skirmish special abiliy is used in a skirmish involving bearer,
 * exert bearer to cancel that action.
 */
public class Card5_065 extends AbstractAttachable {
    public Card5_065() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.ISENGARD, PossessionClass.MOUNT, "Warg");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 3));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        return modifiers;
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.WARG_RIDER;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.activated(game, effectResult, Filters.any)) {
            ActivateCardResult activateEffect = (ActivateCardResult) effectResult;
            if (Filters.inSkirmish.accepts(game, self.getAttachedTo())
                    && activateEffect.getActionTimeword() == Phase.SKIRMISH
                    && PlayConditions.canExert(self, game, Filters.hasAttached(self))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
                action.appendEffect(
                        new CancelActivatedEffect(self, activateEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
