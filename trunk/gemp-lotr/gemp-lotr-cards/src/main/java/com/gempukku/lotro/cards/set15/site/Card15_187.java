package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Hunters
 * Twilight Cost: 0
 * Type: Site
 * Game Text: River. The fellowship archery total is -3.
 */
public class Card15_187 extends AbstractNewSite {
    public Card15_187() {
        super("Anduin River", 0, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.FREE_PEOPLE, -3);
    }
}
