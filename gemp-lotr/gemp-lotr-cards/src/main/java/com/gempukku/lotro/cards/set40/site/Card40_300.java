package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Dimholt Road
 * Set: Second Edition
 * Side: None
 * Site Number: 7
 * Shadow Number: 6
 * Card Number: 1U300
 * Game Text: Mountain. When the fellowship moves to this site without Aragorn, the Free Peoples player must add 2 threats.
 */
public class Card40_300 extends AbstractSite {
    public Card40_300() {
        super("Dimholt Road", Block.SECOND_ED, 7, 6, Direction.LEFT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
        && !PlayConditions.canSpot(game, Filters.aragorn)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(GameUtils.getFreePeoplePlayer(game), self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
