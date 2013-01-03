package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 11
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. While this minion is skirmishing a Free Peoples character who has resistance 5 or less, this
 * minion cannot take wounds.
 */
public class Card11_198 extends AbstractMinion {
    public Card11_198() {
        super(4, 11, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Patrol of Uruk-hai");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self, Filters.and(self, Filters.inSkirmishAgainst(Filters.character, Filters.maxResistance(5))));
    }
}
