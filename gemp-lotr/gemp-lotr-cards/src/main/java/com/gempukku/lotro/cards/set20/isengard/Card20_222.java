package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 4
 * Uruk Brute
 * Isengard	Minion • Uruk-hai
 * 8	2	5
 * Damage +1.
 * While at a battleground, this minion is strength +2.
 */
public class Card20_222 extends AbstractMinion {
    public Card20_222() {
        super(4, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Brute");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), 2);
    }
}
