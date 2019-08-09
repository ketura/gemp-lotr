package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Gandalf, Disturber of the Peace
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion - Wizard
 * Strength: 6
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1C69
 * Game Text: Gandalf is strength +1 for each Hobbit companion you can spot.
 */
public class Card40_069 extends AbstractCompanion{
    public Card40_069() {
        super(4, 6, 4, 8, Culture.GANDALF, Race.WIZARD, null, "Gandalf",
                "Disturber of the Peace", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self, null,
                new CountSpottableEvaluator(Race.HOBBIT, CardType.COMPANION));
        return Collections.singletonList(modifier);
    }
}
