package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: Shadows
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Forest. Each companion is twilight cost +2.
 */
public class Card11_236 extends AbstractNewSite {
    public Card11_236() {
        super("East Road", 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, CardType.COMPANION, 2);
    }
}
