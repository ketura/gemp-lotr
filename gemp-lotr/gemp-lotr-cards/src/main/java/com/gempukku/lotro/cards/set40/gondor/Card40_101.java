package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Boromir, Champion of Minas Tirith
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion - Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Card Number: 1R101
 * Game Text: Ranger. Boromir is strength +1 for each wound on each character in his skirmish.
 */
public class Card40_101 extends AbstractCompanion {
    public Card40_101() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, null, "Boromor",
                "Champion of Minas Tirith", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(
                self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        int woundCount = 0;
                        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Filters.character, Filters.inSkirmish, Filters.wounded)) {
                            woundCount += gameState.getWounds(physicalCard);
                        }
                        return woundCount;
                    }
                });
        return Collections.singletonList(modifier);
    }
}
