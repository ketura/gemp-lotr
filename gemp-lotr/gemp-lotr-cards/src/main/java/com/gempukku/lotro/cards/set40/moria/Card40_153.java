package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountStackedEvaluator;
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
 * Title: *Ancient Chieftain, Elder Goblin
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 4
 * Type: Minion - Goblin
 * Strength: 9
 * Vitality: 3
 * Home: 4
 * Card Number: 1R153
 * Game Text: This minion is strength +1 for each Goblin stacked on a [MORIA] condition.
 */
public class Card40_153 extends AbstractMinion {
    public Card40_153() {
        super(4, 9, 3, 4, Race.GOBLIN, Culture.MORIA, "Ancient Chieftain", "Elder Goblin", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier strengthModifier = new StrengthModifier(self, self,null,
                new CountStackedEvaluator(Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN));
        return Collections.singletonList(strengthModifier);
    }
}
