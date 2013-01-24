package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 4
 * •Gandalf, Disturber of the Peace
 * Gandalf	Companion • Wizard
 * 6	4	7
 * Gandalf is strength +1 for each Hobbit companion you can spot.
 */
public class Card20_156 extends AbstractCompanion {
    public Card20_156() {
        super(4, 6, 4, 7, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "Disturber of the Peace", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new CountSpottableEvaluator(CardType.COMPANION, Race.HOBBIT));
    }
}
