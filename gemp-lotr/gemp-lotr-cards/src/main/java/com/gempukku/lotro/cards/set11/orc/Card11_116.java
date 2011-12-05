package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: While this minion is at an underground site, it is strength +3.
 */
public class Card11_116 extends AbstractMinion {
    public Card11_116() {
        super(2, 7, 1, 4, Race.ORC, Culture.ORC, "Denizen of Moria");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new LocationCondition(Keyword.UNDERGROUND), 3);
    }
}
