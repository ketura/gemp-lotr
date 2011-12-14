package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 4
 * Site: 5
 * Game Text: Damage +1. Each character skirmishing this minion looses all strength bonuses from weapons.
 */
public class Card12_157 extends AbstractMinion {
    public Card12_157() {
        super(5, 9, 4, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk-hai Troop");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CancelStrengthBonusModifier(self, Filters.and(Filters.weapon, Filters.attachedTo(Filters.inSkirmishAgainst(self))));
    }
}
