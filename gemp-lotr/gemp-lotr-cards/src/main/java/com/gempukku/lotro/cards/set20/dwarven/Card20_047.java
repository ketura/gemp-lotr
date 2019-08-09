package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.evaluator.LimitEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Dwarven Ire
 * Dwarven	Condition • Support Area
 * Each Dwarf is damage +1 for each card stacked on this condition (limit +2).
 */
public class Card20_047 extends AbstractPermanent {
    public Card20_047() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, "Dwarven Ire", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, Race.DWARF, null, Keyword.DAMAGE,
                new LimitEvaluator(
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                return game.getGameState().getStackedCards(self).size();
                            }
                        }, 2)));
    }
}
