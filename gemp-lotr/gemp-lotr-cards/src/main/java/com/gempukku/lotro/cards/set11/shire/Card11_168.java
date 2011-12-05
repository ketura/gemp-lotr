package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 9
 * Game Text: For each other unbound companion assigned to a skirmish, Merry is strength +2.
 */
public class Card11_168 extends AbstractCompanion {
    public Card11_168() {
        super(1, 3, 4, 9, Culture.SHIRE, Race.HOBBIT, null, "Merry", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new MultiplyEvaluator(2,
                        new CountActiveEvaluator(Filters.not(self), Filters.unboundCompanion, Filters.assignedAgainst(Filters.any))));
    }
}
