package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Follower
 * Game Text: Aid - Add a threat. (At the start of the maneuver phase, you may add a threat to transfer this to
 * a companion.) When you attach this card to a companion, spot a [GONDOR] possession to heal that companion.
 */
public class Card18_049 extends AbstractFollower {
    public Card18_049() {
        super(Side.FREE_PEOPLE, 1, 0, 0, 0, Culture.GONDOR, "Faramir's Company", null, true);
    }

    @Override
    public boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return PlayConditions.canAddThreat(game, self, 1);
    }

    @Override
    public void appendAidCosts(LotroGame game, CostToEffectAction action, PhysicalCard self) {
        action.appendCost(new AddThreatsEffect(self.getOwner(), self, 1));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, CardType.COMPANION)
                && PlayConditions.canSpot(game, Culture.GONDOR, CardType.POSSESSION)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
