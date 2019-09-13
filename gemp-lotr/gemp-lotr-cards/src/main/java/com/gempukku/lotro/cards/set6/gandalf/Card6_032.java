package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 13
 * Type: Companion • Ent
 * Strength: 10
 * Vitality: 4
 * Resistance: 6
 * Game Text: To play, spot 2 Ent companions. Host of Fangorn's twilight cost is -2 for each Ent or unbound Hobbit
 * you can spot.
 */
public class Card6_032 extends AbstractCompanion {
    public Card6_032() {
        super(13, 10, 4, 6, Culture.GANDALF, Race.ENT, null, "Host of Fangorn", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.ENT, CardType.COMPANION);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return -2 * (Filters.countActive(game, Filters.or(Race.ENT, Filters.and(Race.HOBBIT, Filters.unboundCompanion)))
                + game.getModifiersQuerying().getSpotBonus(game, Race.ENT));
    }
}
