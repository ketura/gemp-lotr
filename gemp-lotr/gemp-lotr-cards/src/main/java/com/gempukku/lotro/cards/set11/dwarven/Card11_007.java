package com.gempukku.lotro.cards.set11.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: While Farin is at a battleground or underground site, he gains muster. (At the start of the regroup phase,
 * you may discard a card from hand to draw a card.)
 */
public class Card11_007 extends AbstractCompanion {
    public Card11_007() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Farin", "Emissary of Erebor", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new LocationCondition(Filters.or(Keyword.BATTLEGROUND, Keyword.UNDERGROUND)), Keyword.MUSTER, 1));
    }
}
