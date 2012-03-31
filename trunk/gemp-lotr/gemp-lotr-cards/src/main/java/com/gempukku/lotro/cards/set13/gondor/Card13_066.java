package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: While you can spot Boromir, Faramir is twilight cost -1. While you can spot Denethor, Faramir is twilight
 * cost -1. While Faramir bears a [GONDOR] possession, he is defender +1.
 */
public class Card13_066 extends AbstractCompanion {
    public Card13_066() {
        super(3, 7, 3, 7, Culture.GONDOR, Race.MAN, null, "Faramir", "Prince of Ithilien", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        int modifier = 0;
        if (Filters.canSpot(gameState, modifiersQuerying, Filters.boromir))
            modifier--;
        if (Filters.canSpot(gameState, modifiersQuerying, Filters.name("Denethor")))
            modifier--;
        return modifier;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, Filters.and(self, Filters.hasAttached(Culture.GONDOR, CardType.POSSESSION)), Keyword.DEFENDER, 1);
    }
}
