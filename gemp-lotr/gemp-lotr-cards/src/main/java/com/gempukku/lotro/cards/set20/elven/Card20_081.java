package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
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
 * Elven Bow
 * Elven	Possession •  Ranged Weapon
 * Bearer must be an Elf.
 * Bearer is an archer.
 */
public class Card20_081 extends AbstractAttachableFPPossession {
    public Card20_081() {
        super(1, 0, 0, Culture.ELVEN, PossessionClass.RANGED_WEAPON, "Elven Bow");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
    }
}
