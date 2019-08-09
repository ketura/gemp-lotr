package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
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
 * Title: Green Hill Country
 * Set: Second Edition
 * Side: None
 * Site Number: 1
 * Shadow Number: 0
 * Card Number: 1U276
 * Game Text: During the fellowship phase, the twilight cost of each Hobbit is -1.
 */
public class Card40_276 extends AbstractSite {
    public Card40_276() {
        super("Green Hill Country", SitesBlock.SECOND_ED, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        TwilightCostModifier modifier = new TwilightCostModifier(self, Race.HOBBIT,
                new PhaseCondition(Phase.FELLOWSHIP), -1);
        return Collections.singletonList(modifier);
    }
}
