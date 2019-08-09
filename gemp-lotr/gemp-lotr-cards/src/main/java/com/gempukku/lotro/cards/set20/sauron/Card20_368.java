package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * Orc Raveners
 * Minion • Orc
 * 12	4	6
 * This minion is strength -1 for each Free Peoples culture you can spot.
 * http://lotrtcg.org/coreset/sauron/orcraveners(r1).png
 */
public class Card20_368 extends AbstractMinion {
    public Card20_368() {
        super(3, 12, 4, 6, Race.ORC, Culture.SAURON, "Orc Ravener");
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self,
                null, new MultiplyEvaluator(-1, new CountFPCulturesEvaluator(self.getOwner())));
    }
}
