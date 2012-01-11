package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: While this minion is at an underground site, it cannot take wounds.
 */
public class Card13_119 extends AbstractMinion {
    public Card13_119() {
        super(2, 6, 1, 4, Race.ORC, Culture.ORC, "Underdeeps Denizen");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self, new LocationCondition(Keyword.UNDERGROUND), self);
    }
}
