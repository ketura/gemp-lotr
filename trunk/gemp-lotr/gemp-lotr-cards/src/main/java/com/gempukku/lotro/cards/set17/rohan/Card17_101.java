package com.gempukku.lotro.cards.set17.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 5
 * Vitality: 3
 * Resistance: 5
 * Game Text: While the Ringbearer is assigned to a skirmish, this companion is strength +2.
 */
public class Card17_101 extends AbstractCompanion {
    public Card17_101() {
        super(2, 5, 3, 5, Culture.ROHAN, Race.MAN, null, "Soldier of Rohan");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(Filters.ringBearer, Filters.assignedToSkirmish), 2);
    }
}
