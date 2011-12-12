package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: This minion is strength +1 for each wound on each companion he is skirmishing (or +2 for each if that
 * companion has resistance 2 or less).
 */
public class Card12_063 extends AbstractMinion {
    public Card12_063() {
        super(3, 9, 2, 4, Race.MAN, Culture.MEN, "Easterling Banner-bearer");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        final PhysicalCard firstActive = Filters.findFirstActive(gameState, modifiersQuerying, Filters.inSkirmishAgainst(CardType.COMPANION, Filters.wounded));
                        if (firstActive != null) {
                            int wounds = gameState.getWounds(firstActive);
                            if (Filters.maxResistance(2).accepts(gameState, modifiersQuerying, firstActive))
                                return 2 * wounds;
                            else
                                return wounds;
                        }

                        return 0;
                    }
                });
    }
}
