package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: While this minion is bearing a possession, each [ORC] minion is strength +1.
 */
public class Card12_087 extends AbstractMinion {
    public Card12_087() {
        super(3, 8, 2, 4, Race.ORC, Culture.ORC, "Goblin Aggressor");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.ORC, CardType.MINION), new SpotCondition(self, Filters.hasAttached(CardType.POSSESSION)), 1);
    }
}
