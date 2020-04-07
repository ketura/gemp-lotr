package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Follower
 * Resistance: +1
 * Game Text: Aid - Add a burden. (At the start of the maneuver phase, you may add a burden to transfer this to
 * a companion.) Each time you transfer this to a companion, you may take a [SHIRE] event into hand from your
 * discard pile.
 */
public class Card13_144 extends AbstractFollower {
    public Card13_144() {
        super(Side.FREE_PEOPLE, 1, 0, 0, 1, Culture.SHIRE, "Daddy Twofoot", "Next-door Neighbor", true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new AddBurdenEffect(self.getOwner(), self, 1));
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, CardType.COMPANION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.SHIRE, CardType.EVENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
