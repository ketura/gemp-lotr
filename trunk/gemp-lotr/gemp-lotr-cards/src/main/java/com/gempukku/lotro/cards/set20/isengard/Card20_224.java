package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 4
 * Relentless Uruk
 * Isengard	Minion • Uruk-hai
 * 8	2	5
 * Damage +1.
 * While at a battleground, this minion is fierce.
 */
public class Card20_224 extends AbstractMinion {
    public Card20_224() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Relentless Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), Keyword.FIERCE, 1);
    }
}
