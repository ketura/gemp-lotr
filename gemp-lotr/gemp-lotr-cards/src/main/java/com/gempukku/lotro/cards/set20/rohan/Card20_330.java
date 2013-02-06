package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.effects.CommonEffects;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * 0
 * Helm's Deep Armory
 * Rohan	Possession • Support Area
 * Fortification.
 * Bearer must be a [Rohan] Man.
 * Bearer may not be overwhelmed unless his or her strength is tripled.
 * Discard this condition at the start of the regroup phase.
 */
public class Card20_330 extends AbstractAttachable {
    public Card20_330() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, 0, Culture.ROHAN, null, "Helm's Deep Armory");
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new OverwhelmedByMultiplierModifier(self, Filters.hasAttached(self), 3);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return CommonEffects.getSelfDiscardAtStartOfRegroup(game, effectResult, self);
    }
}
