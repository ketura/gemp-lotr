package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. While this minion is assigned to a skirmish, each hunter character is strength +1.
 */
public class Card17_123 extends AbstractMinion {
    public Card17_123() {
        super(5, 12, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Captain");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.character, Keyword.HUNTER),
                new SpotCondition(self, Filters.assignedToSkirmish), 1);
    }
}
