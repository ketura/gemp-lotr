package com.gempukku.lotro.cards.set6.dunland;

import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 10
 * Vitality: 1
 * Site: 3
 * Game Text: The twilight cost of this minion during a skirmish phase is -2.
 */
public class Card6_005 extends AbstractMinion {
    public Card6_005() {
        super(4, 10, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Reserve");
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH)
            return -2;
        return 0;
    }
}
