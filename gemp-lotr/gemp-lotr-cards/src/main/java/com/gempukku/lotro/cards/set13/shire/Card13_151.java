package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractFollower;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Follower
 * Strength: +1
 * Game Text: Aid - Add a burden. (At the start of the maneuver phase, you may add a burden to transfer this to
 * a companion.) When you transfer The Gaffer to a [SHIRE] companion, heal bearer (or heal bearer twice if he or she
 * is at a dwelling site.)
 */
public class Card13_151 extends AbstractFollower {
    public Card13_151() {
        super(Side.FREE_PEOPLE, 1, 1, 0, 0, Culture.SHIRE, "The Gaffer", "Master Gardener", true);
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
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, Filters.and(Culture.SHIRE, CardType.COMPANION))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, self.getOwner(), Filters.hasAttached(self)));
            if (PlayConditions.location(game, Keyword.DWELLING))
                action.appendEffect(
                        new HealCharactersEffect(self, self.getOwner(), Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
