package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotCultureTokensCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 5
 * Vitality: 3
 * Resistance: 7
 * Game Text: While you can spot an [ELVEN] token, this companion is strength +2.
 */
public class Card18_009 extends AbstractCompanion {
    public Card18_009() {
        super(2, 5, 3, 7, Culture.ELVEN, Race.ELF, null, "Elven Defender");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new CanSpotCultureTokensCondition(1, Token.ELVEN), 2);
    }
}
