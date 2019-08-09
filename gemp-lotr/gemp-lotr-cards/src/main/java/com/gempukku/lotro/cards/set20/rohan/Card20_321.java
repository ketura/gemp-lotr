package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Eowyn's Sword, Noble Blade
 * Rohan	Possession •   Hand Weapon
 * 3
 * Bearer must be Eowyn.
 * Each time Eowyn wins a skirmish, you may heal an unbound companion.
 */
public class Card20_321 extends AbstractAttachableFPPossession {
    public Card20_321() {
        super(1, 3, 0, Culture.ROHAN, PossessionClass.HAND_WEAPON, "Eowyn's Sword", "Noble Blade", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name(Names.eowyn);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
