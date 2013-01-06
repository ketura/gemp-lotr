package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.evaluator.LimitEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 2
 * •Dwarven Ire
 * Dwarven	Condition • Support Area
 * Each Dwarf is damage +1 for each card stacked on this condition (limit +2).
 */
public class Card20_047 extends AbstractPermanent {
    public Card20_047() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Dwarven Ire", null, true);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new KeywordModifier(self, Race.DWARF, null, Keyword.DAMAGE,
                new LimitEvaluator(
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return gameState.getStackedCards(self).size();
                            }
                        }, 2));
    }
}
