package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Arwen, Fearless Rider
 * Elven	Companion • Elf
 * 6	3	8
 * Ranger.
 * While at a river or forest, Arwen is strength +3
 */
public class Card20_074 extends AbstractCompanion {
    public Card20_074() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Arwen", "Fearless Rider", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new LocationCondition(Filters.or(Keyword.RIVER, Keyword.FOREST)), 3));
}
}
