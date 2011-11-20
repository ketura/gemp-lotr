package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Your opponent may not discard your [SHIRE] tales from play.
 */
public class Card1_306 extends AbstractCompanion {
    public Card1_306() {
        super(1, 3, 4, 6, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Pippin", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantDiscardFromPlayModifier(self, "Can't be discarded by opponent", Filters.and(Culture.SHIRE, Keyword.TALE), Filters.not(Filters.owner(self.getOwner())));
    }
}
