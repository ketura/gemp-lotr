package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotFPCulturesCondition;
import com.gempukku.lotro.logic.modifiers.condition.NotCondition;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Orc Brute
 * Sauron	Minion • Orc
 * 8	3	6
 * While you cannot spot 3 Free Peoples cultures, each [Sauron] minion is strength +2.
 */
public class Card20_364 extends AbstractMinion {
    public Card20_364() {
        super(3, 8, 3, 6, Race.ORC, Culture.SAURON, "Orc Brute");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.and(Culture.SAURON, CardType.MINION),
new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3)), 2));
}
}
