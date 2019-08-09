package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.FPCulturesSideSpotCountModifier;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Citadel of Cirith Ungol
 * 8	8
 * The number of Free Peoples cultures that the Shadow player can spot is -1.
 */
public class Card20_459 extends AbstractSite {
    public Card20_459() {
        super("Citadel of Cirith Ungol", SitesBlock.SECOND_ED, 8, 8, null);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new FPCulturesSideSpotCountModifier(self, Side.SHADOW, -1);
    }
}
