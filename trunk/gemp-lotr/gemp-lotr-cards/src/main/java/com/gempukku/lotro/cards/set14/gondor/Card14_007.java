package com.gempukku.lotro.cards.set14.gondor;

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
 * Set: Expanded Middle-earth
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 5
 * Vitality: 3
 * Resistance: 5
 * Game Text: Ranger. While the fellowship is at a forest or river site, Duilin is strength +3.
 */
public class Card14_007 extends AbstractCompanion {
    public Card14_007() {
        super(2, 5, 3, 5, Culture.GONDOR, Race.MAN, null, "Duilin", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new LocationCondition(Filters.or(Keyword.FOREST, Keyword.RIVER)), 3);
    }
}
