package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.OpponentsCantUseSpecialAbilitiesModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 3
 * •Theoden, Keeper of the Golden Hall
 * Rohan	Companion • Man
 * 7	3	6
 * Valiant.
 * While Theoden is mounted, an opponent may not use skirmish special abilities during skirmishes involving Theoden.
 */
public class Card20_344 extends AbstractCompanion {
    public Card20_344() {
        super(3, 7, 3, 6, Culture.ROHAN, Race.MAN, null, Names.theoden, "Keeper of the Golden Hall", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new OpponentsCantUseSpecialAbilitiesModifier(self,
                new GameHasCondition(self, Filters.mounted, Filters.inSkirmish), self.getOwner());
    }
}
