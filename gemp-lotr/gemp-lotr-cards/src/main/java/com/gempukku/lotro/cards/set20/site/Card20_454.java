package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Eastern Ithilien
 * 7	6
 * Forest.
 * While you can spot a Nazgul, each companion is resistance -1.
 */
public class Card20_454 extends AbstractSite {
    public Card20_454() {
        super("Eastern Ithilien", SitesBlock.SECOND_ED, 7, 6, null);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new ResistanceModifier(self, CardType.COMPANION, new SpotCondition(Race.NAZGUL), -1);
    }
}
