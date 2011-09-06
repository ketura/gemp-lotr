package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 6
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Gandalf is strength +1 for each of these races you can spot in the fellowship: Hobbit, Dwarf, Elf, and
 * Man.
 */
public class Card1_072 extends AbstractCompanion {
    public Card1_072() {
        super(4, 6, 4, Culture.GANDALF, Signet.FRODO, "Gandalf", true);
        addKeyword(Keyword.WIZARD);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Gandalf is strength +1 for each of these races you can spot in the fellowship: Hobbit, Dwarf, Elf, and Man.", Filters.sameCard(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                if (Filters.canSpot(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.HOBBIT)))
                    result++;
                if (Filters.canSpot(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.DWARF)))
                    result++;
                if (Filters.canSpot(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.ELF)))
                    result++;
                if (Filters.canSpot(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.MAN)))
                    result++;
                return result;
            }
        };
    }
}
