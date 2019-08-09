package com.gempukku.lotro.cards.set18.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.CanSpotCultureTokensCondition;

import java.util.Collections;
import java.util.List;

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
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new CanSpotCultureTokensCondition(1, Token.ELVEN), 2));
}
}
