package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

/**
 * Hollin
 * 4	3
 * Plains.
 * Uruk-hai are not roaming.
 */
public class Card20_436 extends AbstractSite {
    public Card20_436() {
        super("Hollin", Block.SECOND_ED, 4, 3, null);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new RemoveKeywordModifier(self, Race.URUK_HAI, Keyword.ROAMING);
    }
}
