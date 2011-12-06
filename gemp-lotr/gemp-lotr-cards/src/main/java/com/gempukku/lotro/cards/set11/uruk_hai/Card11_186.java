package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.RemoveSpecialAbilitiesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. While this minion is assigned to a skirmish, each companion and Free Peoples possession loses
 * its special abilities.
 */
public class Card11_186 extends AbstractMinion {
    public Card11_186() {
        super(3, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Furious Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RemoveSpecialAbilitiesModifier(self,
                new SpotCondition(self, Filters.assignedToSkirmish),
                Filters.or(CardType.COMPANION, Filters.and(Side.FREE_PEOPLE, CardType.POSSESSION)));
    }
}
