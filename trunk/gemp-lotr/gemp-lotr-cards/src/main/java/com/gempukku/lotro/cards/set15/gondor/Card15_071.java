package com.gempukku.lotro.cards.set15.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Ranger. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * This companion is strength +1 for each roaming minion you can spot.
 */
public class Card15_071 extends AbstractCompanion {
    public Card15_071() {
        super(3, 6, 3, 6, Culture.GONDOR, Race.MAN, null, "Unyielding Ranger");
        addKeyword(Keyword.RANGER);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountSpottableEvaluator(CardType.MINION, Keyword.ROAMING));
    }
}
