package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Archer.
 * The twilight cost of each Shadow event and Shadow condition is +1 for each unbound Hobbit you can spot.
 */
public class Card4_073 extends AbstractCompanion {
    public Card4_073() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Legolas", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self,
                        Filters.and(
                                Filters.side(Side.SHADOW),
                                Filters.or(
                                        Filters.type(CardType.EVENT),
                                        Filters.type(CardType.CONDITION)
                                )
                        ), null,
                        new CountSpottableEvaluator(Filters.and(Filters.unboundCompanion(), Filters.race(Race.HOBBIT)))));
    }
}
