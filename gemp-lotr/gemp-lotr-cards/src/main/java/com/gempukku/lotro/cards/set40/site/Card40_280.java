package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Chetwood
 * Set: Second Edition
 * Side: None
 * Site Number: 2
 * Shadow Number: 3
 * Card Number: 1U280
 * Game Text: Forest. The roaming penalty for each Nazgul played at this site is -1.
 */
public class Card40_280 extends AbstractSite {
    public Card40_280() {
        super("Chetwood", Block.SECOND_ED, 2, 3, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        RoamingPenaltyModifier modifier = new RoamingPenaltyModifier(self, Race.NAZGUL,
                new LocationCondition(self), -1);
        return Collections.singletonList(modifier);
    }
}
