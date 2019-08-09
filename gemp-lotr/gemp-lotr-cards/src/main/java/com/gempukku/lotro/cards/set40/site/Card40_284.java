package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Ford of Bruinen
 * Set: Second Edition
 * Side: None
 * Site Number: 3
 * Shadow Number: 0
 * Card Number: 1U284
 * Game Text: River. Sanctuary. The twilight cost of the first Nazgul played at this site each turn is -5.
 */
public class Card40_284 extends AbstractSite {
    public Card40_284() {
        super("Ford of Bruinen", SitesBlock.SECOND_ED, 3, 0, Direction.LEFT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new TwilightCostModifier(self,
                Filters.and(
                        Race.NAZGUL,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                return game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).getUsedLimit() < 1;
                            }
                        }), -5));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Race.NAZGUL)
                && PlayConditions.location(game, self))
            game.getModifiersQuerying().getUntilEndOfTurnLimitCounter(self).incrementToLimit(1, 1);
        return null;
    }
}
