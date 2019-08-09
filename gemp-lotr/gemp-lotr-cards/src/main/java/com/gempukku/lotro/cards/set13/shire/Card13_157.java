package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 3
 * Resistance: 7
 * Game Text: This companion is strength +1 for each [SHIRE] card that has a culture token on it.
 */
public class Card13_157 extends AbstractCompanion {
    public Card13_157() {
        super(2, 3, 3, 7, Culture.SHIRE, Race.HOBBIT, null, "Westfarthing Businessman");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null, new CountActiveEvaluator(Culture.SHIRE, Filters.hasAnyCultureTokens(1))));
}
}
