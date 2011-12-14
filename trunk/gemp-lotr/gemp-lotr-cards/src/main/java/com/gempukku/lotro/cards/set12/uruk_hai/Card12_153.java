package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. While this minion is skirmishing an exhausted companion, this minion is strength +3 (or +5 if
 * that companion has resistance 4 or less).
 */
public class Card12_153 extends AbstractMinion {
    public Card12_153() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Pikeman");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(self, Filters.inSkirmishAgainst(CardType.COMPANION, Filters.exhausted)), null,
                new CardMatchesEvaluator(3, 5, Filters.inSkirmishAgainst(CardType.COMPANION, Filters.exhausted, Filters.maxResistance(4))));
    }
}
