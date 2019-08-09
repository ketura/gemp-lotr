package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Citadel of Cirith Ungol
 * Set: Second Edition
 * Side: None
 * Site Number: 8
 * Shadow Number: 8
 * Card Number: 1C303
 * Game Text: When the fellowship moves to this site without Sam, add a burden.
 */
public class Card40_303 extends AbstractSite {
    public Card40_303() {
        super("Citadel of Cirith Ungol", Block.SECOND_ED, 8, 8, Direction.LEFT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && !PlayConditions.canSpot(game, Filters.sam)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(GameUtils.getFreePeoplePlayer(game), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
