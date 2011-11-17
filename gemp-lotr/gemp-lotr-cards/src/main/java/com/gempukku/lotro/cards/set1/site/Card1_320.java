package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: The Fellowship of the Ring
 * Type: Site
 * Site: 1
 * Game Text: Each companion's twilight cost is +2
 */
public class Card1_320 extends AbstractSite {
    public Card1_320() {
        super("East Road", Block.FELLOWSHIP, 1, 0, Direction.LEFT);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, CardType.COMPANION, 2);
    }
}
