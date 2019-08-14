package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion � Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot a Dwarf. While skirmishing an Orc, Farin is strength +2.
 */
public class Card1_011 extends AbstractCompanion {
    public Card1_011() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Farin", "Dwarven Emissary", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.DWARF);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, Filters.and(self, Filters.inSkirmishAgainst(Race.ORC)), 2));
    }
}
