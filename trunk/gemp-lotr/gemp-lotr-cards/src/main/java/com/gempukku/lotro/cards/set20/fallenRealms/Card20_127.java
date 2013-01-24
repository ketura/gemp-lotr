package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 4
 * Lieutenant of Rhun
 * Fallen Realms	Minion • Man
 * 8	2	4
 * Easterling. Toil 1.
 * This minion is strength +1 for every other Easterling you can spot.
 */
public class Card20_127 extends AbstractMinion {
    public Card20_127() {
        super(4, 8, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Lieutenant of Rhun");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new CountActiveEvaluator(Filters.not(self), Keyword.EASTERLING));
    }
}
