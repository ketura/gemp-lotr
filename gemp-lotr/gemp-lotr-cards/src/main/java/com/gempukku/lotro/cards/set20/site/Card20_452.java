package com.gempukku.lotro.cards.set20.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Broken Gate
 * 7	6
 * Battleground.
 * Each [Sauron] minion is strength +1 and each [Gondor] companion is strength -1.
 */
public class Card20_452 extends AbstractSite {
    public Card20_452() {
        super("Broken Gate", Block.SECOND_ED, 7, 6, null);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.SAURON, CardType.MINION), 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.GONDOR, CardType.COMPANION), -1));
        return modifiers;
    }
}
