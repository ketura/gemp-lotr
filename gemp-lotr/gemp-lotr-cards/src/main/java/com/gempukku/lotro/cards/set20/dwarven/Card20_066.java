package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Ring of Guile, Dwarven Ring of Power
 * Dwarven	Artifact • Ring
 * 1	1
 * Bearer must be a Dwarf.
 * Each time you play a possession on bearer, draw a card.
 */
public class Card20_066 extends AbstractAttachableFPPossession {
    public Card20_066() {
        super(1, 1, 1, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Guile", "Dwarven Ring of Power", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedOn(game, effectResult, Filters.hasAttached(self), CardType.POSSESSION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, self.getOwner(), 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
