package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Rohirrim Shortbow
 * Rohan	Possession •  Ranged Weapon
 * 1
 * Bearer must be a [Rohan] Man. While bearer is at a plains site, add 1 to the fellowship archery total.
 */
public class Card20_340 extends AbstractAttachableFPPossession {
    public Card20_340() {
        super(1, 1, 0, Culture.ROHAN, PossessionClass.RANGED_WEAPON, "Rohirrim Shortbow");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE,
                        new LocationCondition(Keyword.PLAINS), 1));
    }
}
