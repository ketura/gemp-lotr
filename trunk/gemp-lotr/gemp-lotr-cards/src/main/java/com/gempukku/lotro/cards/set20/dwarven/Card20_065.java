package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Ring of Accretion, Dwarven Ring of Power
 * Dwarven	Artifact • Ring
 * 1	1
 * Bearer must be a Dwarf.
 * Each time bearer wins a skirmish, heal another Dwarf.
 */
public class Card20_065 extends AbstractAttachableFPPossession {
    public Card20_065() {
        super(1, 1, 1, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Accretion", "Dwarven Ring of Power", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, self.getOwner(), Filters.not(self.getAttachedTo()), Race.DWARF));
            return Collections.singletonList(action);
        }
        return null;
    }
}
