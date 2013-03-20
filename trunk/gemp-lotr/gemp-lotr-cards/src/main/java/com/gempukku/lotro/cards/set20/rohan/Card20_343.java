package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Snowmane, Royal Stallion
 * Rohan	Possession •  Mount
 * Bearer must be a [Rohan] man.
 * At the start of each skirmish involving bearer, each unmounted minion skirmishing bearer must exert.
 * If bearer is Theoden, each time he wins a skirmish you may liberate a site.
 */
public class Card20_343 extends AbstractAttachableFPPossession {
    public Card20_343() {
        super(1, 0, 0, Culture.ROHAN, PossessionClass.MOUNT, "Snowmane", "Royal Stallion", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.isActive(game, Filters.hasAttached(self), Filters.inSkirmish)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.MINION, Filters.not(Filters.mounted), Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self), Filters.name(Names.theoden))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new LiberateASiteEffect(self, playerId, null));
            return Collections.singletonList(action);
        }
        return null;
    }
}
