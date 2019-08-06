package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Strategist
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion - Goblin
 * Strength: 7
 * Vitality: 2
 * Home: 4
 * Card Number: 1C174
 * Game Text: This minion is strength +1 for each [MORIA] condition with a Goblin stacked on it.
 */
public class Card40_174 extends AbstractMinion {
    public Card40_174() {
        super(3, 7, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Strategist");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self, null,
                new CountSpottableEvaluator(Culture.MORIA, CardType.CONDITION, Filters.hasStacked(Race.GOBLIN)));
        return Collections.singletonList(modifier);
    }
}
