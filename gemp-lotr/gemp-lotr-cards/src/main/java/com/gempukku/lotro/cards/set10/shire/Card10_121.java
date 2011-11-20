package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 12
 * Signet: Frodo
 * Game Text: Ring-bearer (resistance 12).
 */
public class Card10_121 extends AbstractCompanion {
    public Card10_121() {
        super(0, 3, 4, 12, Culture.SHIRE, Race.HOBBIT, Signet.FRODO, "Frodo", true);
        addKeyword(Keyword.CAN_START_WITH_RING);
    }
}
