package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 2
 * Type: Site
 * Site: 2
 * Game Text: While you can spot a ranger, the move limit is +1 for this turn.
 */
public class Card1_327 extends AbstractSite {
    public Card1_327() {
        super("Bree Gate", 2, 2, Direction.LEFT);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot a ranger, the move limit is +1 for this turn.", null) {
            @Override
            public int getMoveLimit(GameState gameState, ModifiersQuerying modifiersQuerying, int result) {
                if (Filters.canSpot(gameState, modifiersQuerying, Filters.keyword(Keyword.RANGER)))
                    return result + 1;
                return result;
            }
        };
    }
}
