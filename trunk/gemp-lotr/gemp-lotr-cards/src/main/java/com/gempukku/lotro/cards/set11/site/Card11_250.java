package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Shadows
 * Twilight Cost: 3
 * Type: Site
 * Game Text: River. The minion archery total is -3.
 */
public class Card11_250 extends AbstractNewSite {
    public Card11_250() {
        super("North Undeep", 3, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.SHADOW, -3);
    }
}
