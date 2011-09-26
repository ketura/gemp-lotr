package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: For each [DWARVEN] tale you can spot, Gloin is strength +1 (limit +4).
 */
public class Card2_007 extends AbstractCompanion {
    public Card2_007() {
        super(2, 5, 3, Culture.DWARVEN, Race.DWARF, null, "Gloin", true);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "For each DWARVEN tale you can spot, Gloin is strength +1 (limit +4)", Filters.sameCard(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int bonus = Math.max(4, Filters.countActive(gameState, modifiersQuerying, Filters.culture(Culture.DWARVEN), Filters.keyword(Keyword.TALE)));
                return result + bonus;
            }
        };
    }
}
