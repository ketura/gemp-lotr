package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * Ranger's Bow
 * Gondor	Possession •  Ranged Weapon
 * 1
 * Bearer must be a [Gondor] ranger. While at a site from your adventure deck, bearer is an archer.
 */
public class Card20_203 extends AbstractAttachableFPPossession {
    public Card20_203() {
        super(1, 1, 0, Culture.GONDOR, PossessionClass.RANGED_WEAPON, "Ranger's Bow");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), new LocationCondition(Filters.owner(self.getOwner())), Keyword.ARCHER, 1));
    }
}
