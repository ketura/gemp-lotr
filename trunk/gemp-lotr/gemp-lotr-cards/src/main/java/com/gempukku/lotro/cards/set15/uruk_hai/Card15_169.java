package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.FierceSkirmishCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
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
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * During a fierce skirmish involving this minion, it is strength +1 for each hunter you can spot.
 */
public class Card15_169 extends AbstractMinion {
    public Card15_169() {
        super(3, 7, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Seeking Uruk", true);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(self, Filters.inSkirmish), new FierceSkirmishCondition(), new CountSpottableEvaluator(Keyword.HUNTER));
    }
}
