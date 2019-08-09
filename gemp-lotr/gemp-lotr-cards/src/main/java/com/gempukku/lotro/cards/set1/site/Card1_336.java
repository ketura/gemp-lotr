package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 2
 * Game Text: Each Nazgul at Weathertop is fierce.
 */
public class Card1_336 extends AbstractSite {
    public Card1_336() {
        super("Weathertop", SitesBlock.FELLOWSHIP, 2, 3, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, Race.NAZGUL, new LocationCondition(self), Keyword.FIERCE, 1));
    }
}
