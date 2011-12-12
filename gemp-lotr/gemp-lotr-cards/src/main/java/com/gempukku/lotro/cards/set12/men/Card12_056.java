package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 7
 * Type: Minion • Man
 * Strength: 14
 * Vitality: 4
 * Site: 4
 * Game Text: Fierce. Toil 1. (For each [MEN] character you exert when playing this, its twilight cost is -1) Each
 * wounded [MEN] minion is strength +2.
 */
public class Card12_056 extends AbstractMinion {
    public Card12_056() {
        super(7, 14, 4, 4, Race.MAN, Culture.MEN, "Castamir of Umbar", true);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Culture.MEN, CardType.MINION, Filters.wounded), 2);
    }
}
