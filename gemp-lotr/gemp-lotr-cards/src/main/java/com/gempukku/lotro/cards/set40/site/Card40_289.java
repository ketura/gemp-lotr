package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Hollin
 * Set: Second Edition
 * Side: None
 * Site Number: 4
 * Shadow Number: 3
 * Card Number: 1U289
 * Game Text: Plains. Uruk-hai are not roaming.
 */
public class Card40_289 extends AbstractSite {
    public Card40_289() {
        super("Hollin", Block.SECOND_ED, 4, 3, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new RemoveKeywordModifier(self, Race.URUK_HAI, Keyword.ROAMING));
    }
}
