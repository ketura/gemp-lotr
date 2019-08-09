package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Haldir, Galadhrim Escort
 * Elven	Companion • Elf
 * 5	3	7
 * While you can spot Galadriel, Haldir is strength +2.
 */
public class Card20_088 extends AbstractCompanion {
    public Card20_088() {
        super(2, 5, 3, 7, Culture.ELVEN, Race.ELF, null, "Haldir", "Galadhrim Escort", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new SpotCondition(Filters.galadriel), 2));
}
}
