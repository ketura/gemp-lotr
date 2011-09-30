package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Damage +1. Each underground site's Shadow number is -2.
 */
public class Card2_121 extends AbstractCompanion {
    public Card2_121() {
        super(2, 6, 3, Culture.DWARVEN, Race.DWARF, Signet.FRODO, "Gimli", true);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.and(Filters.type(CardType.SITE), Filters.keyword(Keyword.UNDERGROUND)), -2));
    }
}
