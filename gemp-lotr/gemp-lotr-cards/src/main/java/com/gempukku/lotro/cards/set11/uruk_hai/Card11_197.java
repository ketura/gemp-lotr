package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
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
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Muster. (At the start of the regroup phase, you may discard a card from hand to draw a card.)
 * While this minion is skirmishing a character who has resistance 3 or less, this minion is strength +3.
 */
public class Card11_197 extends AbstractMinion {
    public Card11_197() {
        super(3, 7, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Overpowering Uruk");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.MUSTER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(self, Filters.inSkirmishAgainst(Filters.character, Filters.maxResistance(3))), 3);
    }
}
