package com.gempukku.lotro.cards.set15.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 7
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Game Text: Leaflock’s twilight cost is -1 for each Ent you can spot. While you can spot 4 other Ents, Leaflock is strength +4.
 */
public class Card15_030 extends AbstractCompanion {
    public Card15_030() {
        super(7, 8, 4, 6, Culture.GANDALF, Race.ENT, null, "Leaflock", "Finglas", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countActive(gameState, modifiersQuerying, Race.ENT)
                - modifiersQuerying.getSpotBonus(gameState, Race.ENT);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new StrengthModifier(self, self,
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return Filters.countActive(gameState, modifiersQuerying, Filters.not(self), Race.ENT)
                                + modifiersQuerying.getSpotBonus(gameState, Race.ENT) >= 4;
                    }
                }, 4);
    }
}
