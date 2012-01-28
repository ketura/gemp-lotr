package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 1
 * Type: Site
 * Site: 2
 * Game Text: Forest. Stealth events may not be played.
 */
public class Card1_329 extends AbstractSite {
    public Card1_329() {
        super("Breeland Forest", Block.FELLOWSHIP, 2, 1, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantPlayCardsModifier(self, CardType.EVENT, Keyword.STEALTH);
    }
}
