package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * Goblin Strategist
 * Moria	Minion • Goblin
 * 7	2	4
 * This minion is strength +1 for each [Moria] condition with a Goblin stacked on it.
 */
public class Card20_273 extends AbstractMinion{
    public Card20_273() {
        super(3, 7, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Strategist");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new CountActiveEvaluator(Culture.MORIA, CardType.CONDITION, Filters.hasStacked(Race.GOBLIN)));
    }
}
