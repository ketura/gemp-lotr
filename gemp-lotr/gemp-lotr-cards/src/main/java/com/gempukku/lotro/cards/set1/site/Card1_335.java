package com.gempukku.lotro.cards.set1.site;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 2
 * Game Text: Each Nazgul's twilight cost is -1.
 */
public class Card1_335 extends AbstractSite {
    public Card1_335() {
        super("Weatherhills", SitesBlock.FELLOWSHIP, 2, 3, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, Race.NAZGUL, -1));
}
}
