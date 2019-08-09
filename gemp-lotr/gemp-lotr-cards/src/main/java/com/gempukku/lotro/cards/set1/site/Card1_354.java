package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: Forest. River. While the fellowship is at Anduin Wilderland, skip the archery phase.
 */
public class Card1_354 extends AbstractSite {
    public Card1_354() {
        super("Anduin Wilderland", SitesBlock.FELLOWSHIP, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new ShouldSkipPhaseModifier(self, new LocationCondition(self), Phase.ARCHERY));
    }
}
