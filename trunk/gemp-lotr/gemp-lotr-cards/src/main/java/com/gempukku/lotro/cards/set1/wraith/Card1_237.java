package com.gempukku.lotro.cards.set1.wraith;

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
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. For each other Nazgul you can spot, The Witch-king is strength +2.
 */
public class Card1_237 extends AbstractMinion {
    public Card1_237() {
        super(8, 14, 4, 3, Keyword.NAZGUL, Culture.WRAITH, "The Witch-king", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "For each other Nazgul you can spot, The Witch-king is strength +2.", Filters.sameCard(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int otherNazgul = Filters.countActive(gameState, modifiersQuerying, Filters.keyword(Keyword.NAZGUL), Filters.not(Filters.sameCard(self)));
                return result + (otherNazgul * 2);
            }
        };
    }
}
