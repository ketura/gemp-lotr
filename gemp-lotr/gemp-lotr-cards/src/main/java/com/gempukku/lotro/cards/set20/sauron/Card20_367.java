package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.conditions.CanSpotFPCulturesCondition;
import com.gempukku.lotro.logic.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 3
 * Orc Ravager
 * Sauron	Minion • Orc
 * 9	3	6
 * While you cannot spot 3 Free Peoples cultures, this minion is damage +1.
 */
public class Card20_367 extends AbstractMinion {
    public Card20_367() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Orc Ravager");
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, self,
                new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3)), Keyword.DAMAGE, 1);
    }
}