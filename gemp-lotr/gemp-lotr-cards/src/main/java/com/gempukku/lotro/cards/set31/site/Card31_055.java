package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.CantSpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Thrór's Throne. Site Card.
 * Site Number: 9
 * Shadow Number: 9
 * 'Mountain. Underground. If you cannot spot [Dwarven] the Arkenstone, the twilight cost of each minion is -1.'
 */
public class Card31_055 extends AbstractSite {
    public Card31_055() {
        super("Thrór's Throne", SitesBlock.HOBBIT, 9, 9, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, CardType.MINION,
new CantSpotCondition(Culture.DWARVEN, Filters.name("The Arkenstone")), -1));
}
}
