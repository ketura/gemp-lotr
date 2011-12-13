package com.gempukku.lotro.cards.set12.rohan;

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
 * Set: Black Rider
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: While this companion is at a battleground or plains site, he is strength +3.
 */
public class Card12_113 extends AbstractCompanion {
    public Card12_113() {
        super(2, 5, 3, 6, Culture.ROHAN, Race.MAN, null, "Eored Warrior");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.PLAINS)), 3);
    }
}
