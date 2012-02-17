package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Bloodlines
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Battleground. While a player can spot more companions than minions, each minion is strength +1.
 */
public class Card13_192 extends AbstractNewSite {
    public Card13_192() {
        super("The Great Gates", 0, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, CardType.MINION,
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION)
                                > Filters.countActive(gameState, modifiersQuerying, CardType.MINION);
                    }
                }, 1);
    }
}
