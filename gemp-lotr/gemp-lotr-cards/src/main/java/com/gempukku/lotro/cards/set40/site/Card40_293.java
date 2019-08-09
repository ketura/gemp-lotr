package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Morannon
 * Set: Second Edition
 * Side: None
 * Site Number: 5
 * Shadow Number: 6
 * Card Number: 1U293
 * Game Text: When the fellowship moves to this site, add 3 threats. When the fellowship moves from this site, remove 3 threats.
 */
public class Card40_293 extends AbstractSite {
    public Card40_293() {
        super("Morannon", SitesBlock.SECOND_ED, 5, 6, Direction.LEFT);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(GameUtils.getFreePeoplePlayer(game), self, 3));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.movesFrom(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RemoveThreatsEffect(self, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
}
