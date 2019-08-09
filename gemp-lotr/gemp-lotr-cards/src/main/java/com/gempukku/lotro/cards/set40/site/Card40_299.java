package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Arrays;
import java.util.List;

/**
 * Title: Broken Gate
 * Set: Second Edition
 * Side: None
 * Site Number: 7
 * Shadow Number: 6
 * Card Number: 1U299
 * Game Text: Battleground. Each [SAURON] minion is strength +1. Each [GONDOR] Man is strength -1.
 */
public class Card40_299 extends AbstractSite {
    public Card40_299() {
        super("Broken Gate", SitesBlock.SECOND_ED, 7, 6, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier sauron = new StrengthModifier(self, Filters.and(Culture.SAURON, CardType.MINION), 1);
        StrengthModifier gondor = new StrengthModifier(self, Filters.and(Culture.GONDOR, Race.MAN), -1);
        return Arrays.asList(sauron, gondor);
    }
}
