package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 2
 * Game Text: Each Nazgul at Weathertop is fierce.
 */
public class Card1_336 extends AbstractSite {
    public Card1_336() {
        super("Weathertop", Block.FELLOWSHIP, 2, 3, Direction.LEFT);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new KeywordModifier(self, Race.NAZGUL, new LocationCondition(self), Keyword.FIERCE, 1);
    }
}
