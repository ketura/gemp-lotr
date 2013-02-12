package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 5
 * •Easterling Captain, Champion of Rhun
 * Fallen Realms	Minion • Man
 * 10	3	4
 * Easterling. Fierce. Toil 2.
 * Each wounded Easterling is strength +1.
 */
public class Card20_112 extends AbstractMinion {
    public Card20_112() {
        super(5, 10, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Captain", "Champion of Rhun", true);
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Keyword.EASTERLING, Filters.wounded), 1);
    }
}
