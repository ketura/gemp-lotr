package com.gempukku.lotro.cards.set40.elven;

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
 * Title: Elven Bow
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession - Ranged Weapon
 * Card Number: 1C42
 * Game Text: Bearer must be an Elf. Bearer is an archer.
 */
public class Card40_042 extends AbstractAttachableFPPossession{
    public Card40_042() {
        super(1, 0, 0, Culture.ELVEN, PossessionClass.RANGED_WEAPON, "Elven Bow");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER);
        return Collections.singletonList(modifier);
    }
}
