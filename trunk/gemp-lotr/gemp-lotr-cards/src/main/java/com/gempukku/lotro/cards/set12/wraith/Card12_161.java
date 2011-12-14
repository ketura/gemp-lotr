package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 8
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. This minion is strength +1 for each companion you can spot.
 */
public class Card12_161 extends AbstractMinion {
    public Card12_161() {
        super(5, 8, 3, 3, Race.NAZGUL, Culture.WRAITH, "Black Rider");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountSpottableEvaluator(CardType.COMPANION));
    }
}
