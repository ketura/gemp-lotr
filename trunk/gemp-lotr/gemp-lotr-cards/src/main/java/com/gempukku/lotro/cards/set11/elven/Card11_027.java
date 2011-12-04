package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: While this companion is at a forest site, he is strength +2.
 */
public class Card11_027 extends AbstractCompanion {
    public Card11_027() {
        super(2, 5, 3, 6, Culture.ELVEN, Race.ELF, null, "Woodland Sentinel");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new LocationCondition(Keyword.FOREST), 2);
    }
}
