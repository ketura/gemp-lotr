package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Ranger. Boromir is not overwhelmed unless his strength is tripled.
 */
public class Card1_096 extends AbstractCompanion {
    public Card1_096() {
        super(3, 7, 3, 6, Culture.GONDOR, Race.MAN, Signet.ARAGORN, "Boromir", "Lord of Gondor", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new OverwhelmedByMultiplierModifier(self, self, 3));
}
}
