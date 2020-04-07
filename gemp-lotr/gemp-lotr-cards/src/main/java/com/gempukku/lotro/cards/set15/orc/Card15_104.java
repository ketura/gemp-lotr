package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 8
 * Type: Minion • Orc
 * Strength: 20
 * Vitality: 4
 * Site: 4
 * Game Text: To play, spot an [ORC] condition. Black Land Shrieker is twilight cost -1 for each threat you spot.
 */
public class Card15_104 extends AbstractMinion {
    public Card15_104() {
        super(8, 20, 4, 4, Race.ORC, Culture.ORC, "Black Land Shrieker", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC, CardType.CONDITION);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return -game.getGameState().getThreats();
    }
}
