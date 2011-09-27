package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 9
 * Type: Site
 * Site: 9
 * Game Text: The twilight cost of each [ISENGARD] minion is -1.
 */
public class Card1_361 extends AbstractSite {
    public Card1_361() {
        super("Slopes of Amon Hen", 9, 9, Direction.LEFT);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.and(Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION)), -1);
    }
}
