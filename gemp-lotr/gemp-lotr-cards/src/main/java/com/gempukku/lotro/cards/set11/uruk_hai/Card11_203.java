package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
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
 * Game Text: Damage +1. While you can spot another [URUK-HAI] minion assigned to a skirmish, this minion cannot take
 * wounds.
 */
public class Card11_203 extends AbstractMinion {
    public Card11_203() {
        super(3, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Swarming Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self, new SpotCondition(Filters.not(self), Culture.URUK_HAI, CardType.MINION, Filters.assignedToSkirmish), self);
    }
}
