package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.CantReplaceSiteModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.List;

/**
 * Title: Eastern Emyn Muil
 * Set: Second Edition
 * Side: None
 * Site Number: 4
 * Shadow Number: 4
 * Card Number: 1U288
 * Game Text: Mountain. Sites in this region may not be replaced.
 */
public class Card40_288 extends AbstractSite {
    public Card40_288() {
        super("Eastern Emyn Muil", Block.SECOND_ED, 4, 4, Direction.LEFT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getWhileInZoneData() == null) {
            int region = GameUtils.getRegion(self.getSiteNumber());
            self.setWhileInZoneData(new Object());
            game.getModifiersEnvironment().addAlwaysOnModifier(
                    new CantReplaceSiteModifier(self, null, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH, Filters.region(region))));
        }
        return null;
    }
}
