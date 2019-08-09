package com.gempukku.lotro.cards.set20.moria;
import java.util.List;
import java.util.Collections;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.evaluator.CountStackedEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 4
 * •Ancient Chieftan, Elder Goblin
 * Moria	Minion • Goblin
 * 9	3	4
 * Ancient Chieftain is strength +1 for each Goblin stacked on a [Moria] condition.
 */
public class Card20_250 extends AbstractMinion {
    public Card20_250() {
        super(4, 9, 3, 4, Race.GOBLIN, Culture.MORIA, "Ancient Chieftain", "Elder Goblin", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null,
new CountStackedEvaluator(Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN)));
}
}
