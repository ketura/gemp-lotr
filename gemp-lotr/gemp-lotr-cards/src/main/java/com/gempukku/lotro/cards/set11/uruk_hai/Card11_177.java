package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 13
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Each Free Peoples archer who has resistance 5 or less does not add to the Free Peoples archery
 * total.
 */
public class Card11_177 extends AbstractMinion {
    public Card11_177() {
        super(5, 13, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Army of Uruk-hai");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new DoesNotAddToArcheryTotalModifier(self, Filters.and(Side.FREE_PEOPLE, Keyword.ARCHER, Filters.maxResistance(5)));
    }
}
