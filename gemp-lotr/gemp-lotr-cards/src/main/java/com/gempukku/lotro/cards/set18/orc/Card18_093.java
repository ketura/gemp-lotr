package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 4
 * Game Text: This minion is strength +1 for each hunter companion you can spot.
 */
public class Card18_093 extends AbstractMinion {
    public Card18_093() {
        super(3, 7, 2, 4, Race.ORC, Culture.ORC, "Wary Orc");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null, new CountActiveEvaluator(CardType.COMPANION, Keyword.HUNTER)));
}
}
