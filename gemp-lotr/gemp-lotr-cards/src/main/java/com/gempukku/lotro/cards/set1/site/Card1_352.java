package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 6
 * Game Text: Forest. Sanctuary. Each ally whose home is site 6 is strength +3.
 */
public class Card1_352 extends AbstractSite {
    public Card1_352() {
        super("Lothlorien Woods", SitesBlock.FELLOWSHIP, 6, 3, Direction.LEFT);
        addKeyword(Keyword.FOREST);

    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.and(CardType.ALLY, Filters.isAllyHome(6, SitesBlock.FELLOWSHIP)), 3));
}
}
