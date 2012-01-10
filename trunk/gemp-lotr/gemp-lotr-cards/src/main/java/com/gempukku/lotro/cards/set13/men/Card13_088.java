package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
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
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: When you play this, you may spot another [MEN] minion to make this minion twilight cost -1 for each wound
 * on the Ring-bearer. While each companion is wounded, this minion is fierce.
 */
public class Card13_088 extends AbstractMinion {
    public Card13_088() {
        super(6, 13, 3, 4, Race.MAN, Culture.MEN, "Dunlending Patriarch");
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Filters.not(self), Culture.MEN, CardType.MINION))
            return -gameState.getWounds(gameState.getRingBearer(gameState.getCurrentPlayerId()));
        return 0;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self, new NotCondition(new SpotCondition(CardType.COMPANION, Filters.unwounded)), Keyword.FIERCE, 1);
    }
}
