package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 3
 * Type: Site
 * Site: 2T
 * Game Text: Each [DUNLAND] Man may not take wounds during the archery phase.
 */
public class Card4_334 extends AbstractSite {
    public Card4_334() {
        super("Rohirrim Village", SitesBlock.TWO_TOWERS, 2, 3, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeWoundsModifier(self, new PhaseCondition(Phase.ARCHERY), Filters.and(Culture.DUNLAND, Race.MAN)));
    }
}
