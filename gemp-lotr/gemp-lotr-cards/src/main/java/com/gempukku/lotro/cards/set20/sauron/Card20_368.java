package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 5
 * Orc Ravener
 * Sauron	Minion • Orc
 * 13	4	6
 * This minion is strength -1 for each Free Peoples culture you can spot.
 */
public class Card20_368 extends AbstractMinion {
    public Card20_368() {
        super(5, 13, 4, 6, Race.ORC, Culture.SAURON, "Orc Ravener");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self,
                null, new MultiplyEvaluator(-1, new CountFPCulturesEvaluator(self.getOwner())));
    }
}
