package com.gempukku.lotro.cards.set40.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Mount Doom
 * Set: Second Edition
 * Side: None
 * Site Number: 9
 * Shadow Number: 9
 * Card Number: 1U308
 * Game Text: Mountain. When the fellowship moves to this site, the Shadow player may remove 2 threats to add 2 burdens.
 */
public class Card40_308 extends AbstractSite {
    public Card40_308() {
        super("Mount Doom", Block.SECOND_ED, 9, 9, Direction.LEFT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesTo(game, effectResult, self)
                && GameUtils.isShadow(game, playerId)
                && PlayConditions.canRemoveThreat(game, self, 2)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 2));
            action.appendEffect(
                    new AddBurdenEffect(playerId, self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
