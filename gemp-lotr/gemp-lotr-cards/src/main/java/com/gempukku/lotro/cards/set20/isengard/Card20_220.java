package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 2
 * Uruk Grunt
 * Isengard	Minion • Uruk-hai
 * 6	1	5
 * Damage +1.
 * While you can spot an [Isengard] Orc, this minion is strength + 3
 */
public class Card20_220 extends AbstractMinion {
    public Card20_220() {
        super(2, 6, 1, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Grunt");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(Culture.ISENGARD, Race.ORC), 3);
    }
}
