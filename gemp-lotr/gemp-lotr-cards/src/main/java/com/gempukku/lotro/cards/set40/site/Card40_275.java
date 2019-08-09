package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: East Road
 * Set: Second Edition
 * Side: None
 * Site Number: 1
 * Shadow Number: 0
 * Card Number: 1U275
 * Game Text: Forest. During the fellowship phase, each companion's twilight cost is +2.
 */
public class Card40_275 extends AbstractSite {
    public Card40_275() {
        super("East Road", SitesBlock.SECOND_ED, 1, 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        TwilightCostModifier modifier = new TwilightCostModifier(self, CardType.COMPANION,
                new PhaseCondition(Phase.FELLOWSHIP), 2);
        return Collections.singletonList(modifier);
    }
}
