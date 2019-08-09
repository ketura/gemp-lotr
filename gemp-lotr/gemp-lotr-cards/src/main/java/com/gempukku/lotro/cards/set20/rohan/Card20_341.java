package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Rohirrim Spear
 * Rohan	Possession •   Hand Weapon
 * 2
 * Bearer must be a [Rohan] man. While bearer is mounted, he is damage +1.
 */
public class Card20_341 extends AbstractAttachableFPPossession {
    public Card20_341() {
        super(1, 2, 0, Culture.ROHAN, PossessionClass.HAND_WEAPON, "Rohirrim Spear");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.mounted), Keyword.DAMAGE, 1));
    }
}
