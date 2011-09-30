package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
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
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 4
 * Site: 6
 * Game Text: For each companion you can spot, this minion is strength +1.
 */
public class Card1_256 extends AbstractMinion {
    public Card1_256() {
        super(5, 9, 4, 6, Race.ORC, Culture.SAURON, "Morgul Hunter");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "For each companion you can spot, this minion is strength +1.", Filters.sameCard(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                int companions = Filters.countSpottable(gameState, modifiersQuerying, Filters.type(CardType.COMPANION));
                return companions + result;
            }
        };
    }
}
