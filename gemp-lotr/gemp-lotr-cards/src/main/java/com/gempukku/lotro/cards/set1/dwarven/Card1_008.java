package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession � Armor
 * Game Text: Bearer must be a Dwarf. Bearer may not be overwhelmed unless his strength is tripled.
 */
public class Card1_008 extends AbstractAttachableFPPossession {
    public Card1_008() {
        super(0, Culture.DWARVEN, Keyword.ARMOR, "Dwarven Armor");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.DWARF);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new OverwhelmedByMultiplierModifier(self, Filters.hasAttached(self), 3);
    }
}
