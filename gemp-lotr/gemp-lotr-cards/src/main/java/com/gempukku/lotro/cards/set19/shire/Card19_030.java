package com.gempukku.lotro.cards.set19.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Game Text: Fellowship. Pippin is strength +1 for each tale you can spot.
 */
public class Card19_030 extends AbstractCompanion {
    public Card19_030() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Pippin", "Steadfast Friend", true);
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountActiveEvaluator(Keyword.TALE));
    }
}
