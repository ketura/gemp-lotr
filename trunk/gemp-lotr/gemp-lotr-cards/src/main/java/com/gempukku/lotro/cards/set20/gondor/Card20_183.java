package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PlayNextSiteEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Blade of Ithilien, Faramir's Sword
 * Gondor	Possession •   Hand Weapon
 * 2
 * Bearer must be Faramir.
 * Each time Faramir wins a skirmish, you may play the fellowship's next site.
 */
public class Card20_183 extends AbstractAttachableFPPossession {
    public Card20_183() {
        super(1, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Blade of Ithilien", "Faramir's Sword", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Faramir");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.name("Faramir"))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PlayNextSiteEffect(action, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
