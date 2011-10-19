package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 2
 * Site: 3
 * Game Text: The twilight cost of this minion during a skirmish phase is -2.
 */
public class Card6_003 extends AbstractMinion {
    public Card6_003() {
        super(5, 11, 2, 3, Race.MAN, Culture.DUNLAND, "Dunlending Footman");
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (gameState.getCurrentPhase() == Phase.SKIRMISH)
            return -2;
        return 0;
    }
}
