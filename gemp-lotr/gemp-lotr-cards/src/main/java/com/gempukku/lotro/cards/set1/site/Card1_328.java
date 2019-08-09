package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 1
 * Type: Site
 * Site: 2
 * Game Text: The roaming penalty for each Nazgul you play to Bree Streets is -2.
 */
public class Card1_328 extends AbstractSite {
    public Card1_328() {
        super("Bree Streets", SitesBlock.FELLOWSHIP, 2, 1, Direction.LEFT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new RoamingPenaltyModifier(self,
                Race.NAZGUL, new LocationCondition(self), -2);
    }
}
