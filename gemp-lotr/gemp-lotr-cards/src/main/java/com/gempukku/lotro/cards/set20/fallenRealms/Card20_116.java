package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * ❸ Easterling Pikeman [Fal]
 * Minion • Man
 * Strength: 8   Vitality: 2   Roaming: 4
 * Easterling.
 * While bearing a [Fal] weapon, this minion is damage +1.
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/easterlingpikeman(r3).jpg
 */
public class Card20_116 extends AbstractMinion {
    public Card20_116() {
        super(3, 8, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Pikeman");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(self, Filters.hasAttached(Filters.weapon, Culture.FALLEN_REALMS)), Keyword.DAMAGE, 1);
    }
}
