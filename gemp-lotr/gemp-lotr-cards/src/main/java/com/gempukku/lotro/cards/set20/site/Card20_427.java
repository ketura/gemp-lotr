package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Weathertop
 * 2	3
 * Battleground.
 * Nazgul are fierce.
 */
public class Card20_427 extends AbstractSite {
    public Card20_427() {
        super("Weathertop", SitesBlock.SECOND_ED, 2, 3, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Race.NAZGUL, Keyword.FIERCE));
}
}
