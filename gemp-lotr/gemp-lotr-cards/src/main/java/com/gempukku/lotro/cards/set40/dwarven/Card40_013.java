package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.evaluator.CountStackedEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.LimitEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Dwarven Ire
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1R13
 * Game Text: Each Dwarf is damage +1 for each card stacked on this condition (limit +2).
 */
public class Card40_013 extends AbstractPermanent {
    public Card40_013() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Dwarven Ire", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier keywordModifier = new KeywordModifier(self, Race.DWARF, null, Keyword.DAMAGE,
                new CountStackedEvaluator(2, self, Filters.any));
        return Collections.singletonList(keywordModifier);
    }
}
