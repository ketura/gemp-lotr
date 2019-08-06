package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.cards.set40.isengard.Card40_130;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Grudgebearer
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion - Goblin
 * Strength: 8
 * Vitality: 1
 * Home: 4
 * Card Number: 1C161
 * Game Text: This minion is strength +1 for each Dwarf you can spot.
 */
public class Card40_161 extends AbstractMinion {
    public Card40_161() {
        super(3, 8, 1, 4, Race.GOBLIN, Culture.MORIA, "Goblin Grudgebearer");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self, null,
                new CountSpottableEvaluator(Race.DWARF));
        return Collections.singletonList(modifier);
    }
}
