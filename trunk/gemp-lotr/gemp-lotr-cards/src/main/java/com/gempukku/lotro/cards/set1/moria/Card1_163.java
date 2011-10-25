package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion o Orc
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: For each other [MORIA] Orc you can spot, Ancient Chieftain is strength +1.
 */
public class Card1_163 extends AbstractMinion {
    public Card1_163() {
        super(4, 9, 2, 4, Race.ORC, Culture.MORIA, "Ancient Chieftain", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountSpottableEvaluator(Culture.MORIA, Race.ORC, Filters.not(self)));
    }
}
