package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 2
 * •Eowyn, Lady of the Shield-arm
 * Companion • Man
 * 6	3	7
 * Valiant.
 * While you can spot a [Rohan] Man, Eowyn's twilight cost is -1.
 * While Eowyn is exhausted, she is strength +2.
 * http://lotrtcg.org/coreset/rohan/eowynlots(r2).jpg
 */
public class Card20_319 extends AbstractCompanion {
    public Card20_319() {
        super(2, 6, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Lady of the Shield-arm", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.ROHAN, Race.MAN)?-1:0;
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(self, Filters.exhausted), 2);
    }
}
