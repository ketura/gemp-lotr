package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a [MEN] minion. When you play this possession you may reinforce a [MEN] token twice.
 * Each time a companion is killed during a skirmish phase, you may control a site.
 */
public class Card18_062 extends AbstractAttachable {
    public Card18_062() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.MEN, PossessionClass.HAND_WEAPON, "Corsair Grappling Hook");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, CardType.MINION);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.MEN));
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.MEN));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.forEachKilled(game, effectResult, CardType.COMPANION)
                && PlayConditions.isPhase(game, Phase.SKIRMISH)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
