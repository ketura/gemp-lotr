package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be an [ISENGARD] Orc. When you play this weapon, you may add (1) for each site you control.
 */
public class Card5_054 extends AbstractAttachable {
    public Card5_054() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.ISENGARD, PossessionClass.HAND_WEAPON, "Isengard Scimitar");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ISENGARD, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId)) > 0) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
