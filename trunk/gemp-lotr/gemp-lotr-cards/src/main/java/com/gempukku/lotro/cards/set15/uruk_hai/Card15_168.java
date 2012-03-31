package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 5
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * Searching Uruk is strength +1 for each other [URUK-HAI] hunter you can spot.
 */
public class Card15_168 extends AbstractMinion {
    public Card15_168() {
        super(2, 5, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Searching Uruk", null, true);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountActiveEvaluator(Filters.not(self), Culture.URUK_HAI, Keyword.HUNTER));
    }
}
