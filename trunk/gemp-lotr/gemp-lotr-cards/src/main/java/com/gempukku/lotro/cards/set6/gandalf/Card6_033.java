package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Game Text: Quickbeam's twilight cost is -1 for each Ent and unbound Hobbit you can spot.
 */
public class Card6_033 extends AbstractCompanion {
    public Card6_033() {
        super(4, 8, 3, 6, Culture.GANDALF, Race.ENT, null, "Quickbeam", true);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countSpottable(gameState, modifiersQuerying, Filters.or(Race.ENT, Filters.and(Race.HOBBIT, Filters.unboundCompanion)));
    }
}
