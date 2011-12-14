package com.gempukku.lotro.cards.set12.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: Black Rider
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Battleground. Underground. The Balrog is twilight cost -3.
 */
public class Card12_186 extends AbstractNewSite {
    public Card12_186() {
        super("The Bridge of Khazad-dum", 0, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.balrog, -3);
    }
}
