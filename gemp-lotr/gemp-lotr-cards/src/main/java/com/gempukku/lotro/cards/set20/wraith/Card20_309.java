package com.gempukku.lotro.cards.set20.wraith;
import java.util.List;
import java.util.Collections;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.FpSkirmishResistanceStrengthOverrideModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 4
 * •Ulaire Nertea, Twilight Fiend
 * Minion • Nazgul
 * 9	2	3
 * Twilight.
 * Companions bearing [Ringwraith] conditions use resistance instead of strength to resolve skirmishes involving Ulaire Nertea.
 * http://lotrtcg.org/coreset/ringwraith/ulairenerteatf(r1).jpg
 */
public class Card20_309 extends AbstractMinion {
    public Card20_309() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.nertea, "Twilight Fiend", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new FpSkirmishResistanceStrengthOverrideModifier(self,
Filters.and(CardType.COMPANION, Filters.hasAttached(Culture.WRAITH, CardType.CONDITION), Filters.inSkirmishAgainst(self)), null));
}
}
