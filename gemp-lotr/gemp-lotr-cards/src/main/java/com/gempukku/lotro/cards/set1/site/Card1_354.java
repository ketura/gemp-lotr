package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.ShouldSkipPhaseModifier;
import com.gempukku.lotro.logic.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 7
 * Game Text: Forest. River. While the fellowship is at Anduin Wilderland, skip the archery phase.
 */
public class Card1_354 extends AbstractSite {
    public Card1_354() {
        super("Anduin Wilderland", Block.FELLOWSHIP, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.FOREST);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new ShouldSkipPhaseModifier(self, new LocationCondition(self), Phase.ARCHERY);
    }
}
