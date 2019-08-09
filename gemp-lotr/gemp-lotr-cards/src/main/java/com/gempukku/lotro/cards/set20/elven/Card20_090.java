package com.gempukku.lotro.cards.set20.elven;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 2
 * •Legolas, Son of Thranduil
 * Elven	Companion • Elf
 * 6	3	8
 * Archer.
 * While Legolas is at a river or forest, add 1 to the fellowship archery total.
 */
public class Card20_090 extends AbstractCompanion {
    public Card20_090() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Legolas", "Son of Thranduil", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ArcheryTotalModifier(self, Side.FREE_PEOPLE, new LocationCondition(Filters.or(Keyword.RIVER, Keyword.FOREST)), 1));
}
}
