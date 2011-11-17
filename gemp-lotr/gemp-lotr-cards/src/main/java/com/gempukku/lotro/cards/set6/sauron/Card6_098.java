package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
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
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be an [SAURON] Orc. Each time bearer wins a skirmish, you may add (1) for each site
 * you control.
 */
public class Card6_098 extends AbstractAttachable {
    public Card6_098() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.SAURON, PossessionClass.HAND_WEAPON, "Banner of the Eye");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SAURON, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId));
            action.appendEffect(
                    new AddTwilightEffect(self, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
