package com.gempukku.lotro.cards.set13.shire;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new AddBurdenEffect(self.getOwner(), self, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, Filters.and(Culture.SHIRE, CardType.COMPANION))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.hasAttached(self)));
            if (PlayConditions.location(game, Keyword.DWELLING))
                action.appendEffect(
                        new HealCharactersEffect(self, Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
