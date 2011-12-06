package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 6
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Lurker. (Skirmishes involving lurker minions must be resolved after any others.) This minion
 * is strength +2 for each [URUK-HAI] minion not assigned to a skirmish.
 */
public class Card11_205 extends AbstractMinion {
    public Card11_205() {
        super(2, 6, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Vigilant Uruk");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.LURKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new MultiplyEvaluator(2, new CountActiveEvaluator(Culture.URUK_HAI, CardType.MINION, Filters.notAssignedToSkirmish)));
    }
}
