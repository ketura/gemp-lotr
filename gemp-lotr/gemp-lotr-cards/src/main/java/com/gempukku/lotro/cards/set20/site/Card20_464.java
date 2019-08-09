package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * The Black Gate
 * 9	9
 * Battleground.
 * Free People's events may not be played.
 */
public class Card20_464 extends AbstractSite {
    public Card20_464() {
        super("The Black Gate", Block.SECOND_ED, 9, 9, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new CantPlayCardsModifier(self, Side.FREE_PEOPLE, CardType.EVENT);
    }
}
