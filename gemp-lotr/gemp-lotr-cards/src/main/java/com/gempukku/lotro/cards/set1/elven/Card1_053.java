package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Companion • Elf
 * Strength: 4
 * Vitality: 2
 * Resistance: 6
 * Game Text: To play, spot an Elf.
 */
public class Card1_053 extends AbstractCompanion {
    public Card1_053() {
        super(1, 4, 2, Culture.ELVEN, Race.ELF, null, "Lorien Elf");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }
}
