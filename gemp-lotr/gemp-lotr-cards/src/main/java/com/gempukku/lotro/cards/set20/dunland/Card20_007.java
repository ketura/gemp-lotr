package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 1
 * Dunlending Veteran
 * Minion • Man
 * 4	1	3
 * While you control a site, this minion is strength +3.
 * http://lotrtcg.org/coreset/dunland/dunlendingveteran(r1).png
 */
public class Card20_007 extends AbstractMinion {
    public Card20_007() {
        super(1, 4, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Veteran");
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, new GameHasCondition(Filters.siteControlled(self.getOwner())), 3);
    }
}
