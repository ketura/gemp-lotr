package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: Forest. River. While the fellowship is at Anduin Wilderland, skip the archery phase.
 */
public class Card1_354 extends AbstractSite {
    public Card1_354() {
        super("Anduin Wilderland", 7, 6, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new AbstractModifier(self, "Skip archery phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
            @Override
            public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId, boolean result) {
                if (phase == Phase.ARCHERY
                        && gameState.getCurrentSite() == self)
                    return true;
                return result;
            }
        };
    }
}
