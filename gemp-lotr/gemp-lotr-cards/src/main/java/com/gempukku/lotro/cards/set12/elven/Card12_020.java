package com.gempukku.lotro.cards.set12.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: While Orophin is at a forest site and you can spot another Elf, Orophin is an archer.
 */
public class Card12_020 extends AbstractCompanion {
    public Card12_020() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Orophin", "Brother of Haldir", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self,
                new AndCondition(
                        new LocationCondition(Keyword.FOREST),
                        new SpotCondition(Filters.not(self), Race.ELF)), Keyword.ARCHER, 1);
    }
}
