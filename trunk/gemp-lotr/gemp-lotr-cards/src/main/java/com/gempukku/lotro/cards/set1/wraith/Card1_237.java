package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Fierce. For each other Nazgul you can spot, The Witch-king is strength +2.
 */
public class Card1_237 extends AbstractMinion {
    public Card1_237() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, "The Witch-king", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new StrengthModifier(self, self, null, new MultiplyEvaluator(2, new CountSpottableEvaluator(Race.NAZGUL, Filters.not(self))));
    }
}
