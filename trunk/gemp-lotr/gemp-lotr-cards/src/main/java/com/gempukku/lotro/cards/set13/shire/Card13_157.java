package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 3
 * Resistance: 7
 * Game Text: This companion is strength +1 for each [SHIRE] card that has a culture token on it.
 */
public class Card13_157 extends AbstractCompanion {
    public Card13_157() {
        super(2, 3, 3, 7, Culture.SHIRE, Race.HOBBIT, null, "Westfarthing Businessman");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountActiveEvaluator(Culture.SHIRE, Filters.hasAnyCultureTokens(1)));
    }
}
