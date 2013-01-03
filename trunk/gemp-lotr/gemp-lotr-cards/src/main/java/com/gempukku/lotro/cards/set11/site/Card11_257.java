package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

/**
 * Set: Shadows
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Plains. During the archery phase, [URUK] minions cannot take wounds.
 */
public class Card11_257 extends AbstractNewSite {
    public Card11_257() {
        super("Rohan Uplands", 1, Direction.RIGHT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeWoundsModifier(self, new PhaseCondition(Phase.ARCHERY), Filters.and(Culture.URUK_HAI, CardType.MINION));
    }
}
