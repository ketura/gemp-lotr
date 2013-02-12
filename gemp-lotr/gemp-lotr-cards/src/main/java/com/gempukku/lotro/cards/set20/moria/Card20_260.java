package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * Goblin Grudgebearer
 * Moria	Minion • Goblin
 * 8	1	4
 * This minion is strength +1 for each Dwarf you can spot.
 */
public class Card20_260 extends AbstractMinion {
    public Card20_260() {
        super(3, 8, 1, 4, Race.GOBLIN, Culture.MORIA, "Goblin Grudgebearer");
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountSpottableEvaluator(Race.DWARF));
    }
}
