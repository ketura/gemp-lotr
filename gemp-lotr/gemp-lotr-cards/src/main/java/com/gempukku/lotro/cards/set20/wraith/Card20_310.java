package com.gempukku.lotro.cards.set20.wraith;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 4
 * •Ulaire Otsea, Twilight Menace
 * Ringwraith	Minion • Nazgul
 * 9	3	3
 * Twilight. Each companion bearing a [Ringwraith] condition is resistance -2.
 */
public class Card20_310 extends AbstractMinion {
    public Card20_310() {
        super(4, 9, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.otsea, "Twilight Menace", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ResistanceModifier(self, Filters.and(CardType.COMPANION, Filters.hasAttached(Culture.WRAITH, CardType.CONDITION)), -2));
}
}
