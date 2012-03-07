package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotCultureTokensCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: Ranger. While you can spot a [GONDOR] token, this companion is strength +2.
 */
public class Card18_056 extends AbstractCompanion {
    public Card18_056() {
        super(2, 5, 3, 6, Culture.GONDOR, Race.MAN, null, "Ranger of the South");
        addKeyword(Keyword.RANGER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new CanSpotCultureTokensCondition(1, Token.GONDOR), 2);
    }
}
