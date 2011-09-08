package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: For each other [MORIA] Orc you can spot, Ancient Chieftain is strength +1.
 */
public class Card1_163 extends AbstractMinion {
    public Card1_163() {
        super(4, 9, 2, 4, Keyword.ORC, Culture.MORIA, "Ancient Chieftain", true);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "For each other MORIA Orc you can spot, Ancient Chieftain is strength +1.", Filters.sameCard(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + Filters.countActive(gameState, modifiersQuerying, Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC), Filters.not(Filters.sameCard(physicalCard)));
            }
        };
    }
}
