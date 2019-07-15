package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.evaluator.AddEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.LimitEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MinEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.VitalityEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.CardStackedCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Dwarven Vigor
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1R14
 * Game Text: While there is a [DWARVEN] card stacked on this condition, each Dwarf is strength +1 for each point of vitality over 3 that he has.
 */
public class Card40_014 extends AbstractPermanent {
    public Card40_014(){
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Dwarven Vigor", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, Race.DWARF,
                new CardStackedCondition(1, self, Culture.DWARVEN),
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return Math.max(0, modifiersQuerying.getVitality(gameState, cardAffected) - 3);
                    }
                });
        return Collections.singletonList(modifier);
    }
}
