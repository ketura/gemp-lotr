package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotFPCulturesCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 3
 * Orc Brute
 * Sauron	Minion • Orc
 * 10	3	6
 * While you cannot spot 3 Free Peoples cultures, each [Sauron] minion is strength +2.
 */
public class Card20_364 extends AbstractMinion {
    public Card20_364() {
        super(3, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Brute");
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.SAURON, CardType.MINION),
                new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3)), 2);
    }
}
