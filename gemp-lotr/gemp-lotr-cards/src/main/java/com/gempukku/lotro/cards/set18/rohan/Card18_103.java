package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotCultureTokensCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. While you can spot a [ROHAN] token, this companion is strength +2.
 */
public class Card18_103 extends AbstractCompanion {
    public Card18_103() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, null, "Rohirrim Recruit");
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, new CanSpotCultureTokensCondition(1, Token.ROHAN), 2);
    }
}
