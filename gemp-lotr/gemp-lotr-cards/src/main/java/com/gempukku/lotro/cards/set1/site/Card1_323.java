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

/**
 * Set: The Fellowship of the Ring
 * Type: Site
 * Site: 1
 * Game Text: The twilight cost of each Hobbit is -1.
 */
public class Card1_323 extends AbstractSite {
    public Card1_323() {
        super("Green Hill Country", SitesBlock.FELLOWSHIP, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new TwilightCostModifier(self, Race.HOBBIT, -1));
}
}
