package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: For each [DWARVEN] tale you can spot, Gloin is strength +1 (limit +4).
 */
public class Card2_007 extends AbstractCompanion {
    public Card2_007() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Gloin", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountSpottableEvaluator(4, Culture.DWARVEN, Keyword.TALE));
    }
}
