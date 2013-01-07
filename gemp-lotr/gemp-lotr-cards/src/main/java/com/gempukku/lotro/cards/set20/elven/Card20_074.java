package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 2
 * •Arwen, Fearless Rider
 * Elven	Companion • Elf
 * 6	3	8
 * Ranger.
 * While at a river or forest, Arwen is strength +3
 */
public class Card20_074 extends AbstractCompanion {
    public Card20_074() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Arwen", "Fearless Rider", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new LocationCondition(Filters.or(Keyword.RIVER, Keyword.FOREST)), 3);
    }
}
