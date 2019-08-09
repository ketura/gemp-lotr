package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Barrow-downs
 * 2	2
 * While the Shadow player has initaitive, this site's shadow number is +3.
 */
public class Card20_422 extends AbstractSite {
    public Card20_422() {
        super("Barrow-downs", SitesBlock.SECOND_ED, 2, 2, null);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        return (game.getModifiersQuerying().hasInitiative(game) == Side.SHADOW) ? 3 : 0;
    }
}
