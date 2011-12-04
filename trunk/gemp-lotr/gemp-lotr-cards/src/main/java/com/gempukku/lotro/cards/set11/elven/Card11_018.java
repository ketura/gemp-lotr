package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.filters.Filters;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 7
 * Game Text: While you can spot another Elf, this companion has muster. (At the start of the regroup phase, you may
 * discard a card from hand to draw a card.)
 */
public class Card11_018 extends AbstractCompanion {
    public Card11_018() {
        super(2, 6, 3, 7, Culture.ELVEN, Race.ELF, null, "Elven Scout");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self, new SpotCondition(Filters.not(self), Race.ELF), Keyword.MUSTER, 1);
    }
}
