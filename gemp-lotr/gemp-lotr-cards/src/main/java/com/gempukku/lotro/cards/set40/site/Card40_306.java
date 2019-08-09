package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: The Black Gate
 * Set: Second Edition
 * Side: None
 * Site Number: 9
 * Shadow Number: 9
 * Card Number: 1U306
 * Game Text: Battleground: Free Peoples events may not be played.
 */
public class Card40_306 extends AbstractSite {
    public Card40_306() {
        super("The Black Gate", SitesBlock.SECOND_ED, 9, 9, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        CantPlayCardsModifier modifier = new CantPlayCardsModifier(self, Side.FREE_PEOPLE, CardType.EVENT);
        return Collections.singletonList(modifier);
    }
}
