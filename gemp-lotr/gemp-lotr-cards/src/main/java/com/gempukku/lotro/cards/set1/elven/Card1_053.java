package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;

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
        super(1, 4, 2, 6, Culture.ELVEN, Race.ELF, null, "Lorien Elf");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.ELF);
    }
}
