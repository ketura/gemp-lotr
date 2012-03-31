package com.gempukku.lotro.cards.set19.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.CantDiscardCardsFromHandOrTopOfDeckModifier;
import com.gempukku.lotro.cards.modifiers.MaxThreatCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Game Text: Fellowship. Ring-bearer. Ring-bound. While you cannot spot 4 threats, Shadow cards cannot discard cards
 * from your hand or from the top of your draw deck.
 */
public class Card19_028 extends AbstractCompanion {
    public Card19_028() {
        super(0, 3, 4, 10, Culture.SHIRE, Race.HOBBIT, null, "Frodo", "Little Master", true);
        addKeyword(Keyword.FELLOWSHIP);
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantDiscardCardsFromHandOrTopOfDeckModifier(self, new MaxThreatCondition(3), self.getOwner(), Side.SHADOW);
    }
}
