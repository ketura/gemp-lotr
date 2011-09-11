package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
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
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: For each other [SAURON] Orc you can spot, Morgul Warden is strength +1.
 */
public class Card1_259 extends AbstractMinion {
    public Card1_259() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Morgul Warden", true);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "For each other [SAURON] Orc you can spot, Morgul Warden is strength +1.", Filters.sameCard(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int otherSauronOrcs = Filters.countActive(gameState, modifiersQuerying, Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.not(Filters.sameCard(self)));
                return otherSauronOrcs + result;
            }
        };
    }
}
