package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 1
 * Type: Site
 * Site: 2
 * Game Text: River. While only Hobbits are in the fellowship, there are no assignment and skirmish phases at
 * Buckleberry Ferry.
 */
public class Card1_330 extends AbstractSite {
    public Card1_330() {
        super("Buckleberry Ferry", 2, 1, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "While only Hobbits are in the fellowship, there are no assignment and skirmish phases at Buckleberry Ferry.", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
            @Override
            public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result) {
                if (gameState.getCurrentSite() == self
                        && (phase == Phase.ASSIGNMENT || phase == Phase.SKIRMISH)
                        && !Filters.canSpot(gameState, modifiersQuerying, Filters.type(CardType.COMPANION), Filters.not(Filters.race(Race.HOBBIT))))
                    return true;
                return result;
            }
        };
    }
}
