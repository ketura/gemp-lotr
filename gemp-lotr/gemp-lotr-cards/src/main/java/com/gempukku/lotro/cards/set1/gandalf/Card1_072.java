package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

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
        super(4, 6, 4, Culture.GANDALF, Race.WIZARD, Signet.FRODO, "Gandalf", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
                        int result = 0;
                        if (Filters.canSpot(gameState, modifiersQuerying, CardType.COMPANION, Race.HOBBIT))
                            result++;
                        if (Filters.canSpot(gameState, modifiersQuerying, CardType.COMPANION, Race.DWARF))
                            result++;
                        if (Filters.canSpot(gameState, modifiersQuerying, CardType.COMPANION, Race.ELF))
                            result++;
                        if (Filters.canSpot(gameState, modifiersQuerying, CardType.COMPANION, Race.MAN))
                            result++;
                        return result;
                    }
                });
    }
}
