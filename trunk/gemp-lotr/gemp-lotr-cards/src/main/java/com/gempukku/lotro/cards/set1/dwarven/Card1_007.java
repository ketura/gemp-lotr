package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Companion � Dwarf
 * Strength: 4
 * Vitality: 2
 * Resistance: 6
 * Game Text: To play, spot a Dwarf.
 */
public class Card1_007 extends AbstractCompanion {
    public Card1_007() {
        super(1, 4, 2, Culture.DWARVEN, "Dwarf Guard", false);
        addKeyword(Keyword.DWARF);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return super.checkPlayRequirements(playerId, game, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF));
    }
}
