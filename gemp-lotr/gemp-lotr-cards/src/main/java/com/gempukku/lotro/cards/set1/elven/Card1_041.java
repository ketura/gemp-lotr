package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession � Ranged Weapon
 * Game Text: Bearer must be an Elf. Bearer is an archer.
 */
public class Card1_041 extends AbstractAttachableFPPossession {
    public Card1_041() {
        super(1, Culture.ELVEN, Keyword.RANGED_WEAPON, "Elven Bow");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.ELF);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER);
    }
}
