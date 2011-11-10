package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

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
        super(13, 10, 4, Culture.GANDALF, Race.ENT, null, "Host of Fangorn", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Race.ENT, CardType.COMPANION);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -2 * Filters.countSpottable(gameState, modifiersQuerying, Filters.or(Race.ENT, Filters.and(Race.HOBBIT, Filters.unboundCompanion)));
    }
}
