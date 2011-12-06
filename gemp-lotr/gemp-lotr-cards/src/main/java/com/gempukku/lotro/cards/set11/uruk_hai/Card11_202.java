package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. While this minion is skirmishing a character who has resistance 4 or less, this minion is
 * damage +1.
 */
public class Card11_202 extends AbstractMinion {
    public Card11_202() {
        super(4, 10, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Squad of Uruk-hai");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(self, Filters.inSkirmishAgainst(Filters.character, Filters.maxResistance(4))), Keyword.DAMAGE, 1);
    }
}
