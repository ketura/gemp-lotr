package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.FpSkirmishResistanceStrengthOverrideModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 4
 * •Ulaire Nertea, Twilight Fiend
 * Ringwraith	Minion • Nazgul
 * 9	3	3
 * Twilight. Characters skirmishing Ulaire Nertea use resistance instead of strength to resolve skirmishes.
 */
public class Card20_309 extends AbstractMinion {
    public Card20_309() {
        super(4, 9, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.nertea, "Twilight Fiend", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new FpSkirmishResistanceStrengthOverrideModifier(self, Filters.and(Filters.character, Filters.inSkirmishAgainst(self)), null);
    }
}
