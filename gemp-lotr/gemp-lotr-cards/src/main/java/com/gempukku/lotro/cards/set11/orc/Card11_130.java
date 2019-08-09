package com.gempukku.lotro.cards.set11.orc;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
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
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be an [ORC] Orc. When you play this possession, you may exert a companion.
 */
public class Card11_130 extends AbstractAttachable {
    public Card11_130() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.ORC, PossessionClass.HAND_WEAPON, "Orc Hammer");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ORC, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.hasAttached(self), 1));
}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
