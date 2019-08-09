package com.gempukku.lotro.cards.set20.isengard;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
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
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Brute");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), 2));
}
}
