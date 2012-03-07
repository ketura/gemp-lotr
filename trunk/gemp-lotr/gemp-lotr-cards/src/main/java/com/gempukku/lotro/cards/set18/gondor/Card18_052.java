package com.gempukku.lotro.cards.set18.gondor;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Follower
 * Resistance: +3
 * Game Text: Aid - (1). (At the start of the maneuver phase, you may add (1) to transfer this to a companion.)
 * Each time you attach this to a companion, except a [GONDOR] Man, add (4).
 */
public class Card18_052 extends AbstractFollower {
    public Card18_052() {
        super(Side.FREE_PEOPLE, 0, 0, 0, 3, Culture.GONDOR, "Gondorian Servant", true);
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new AddTwilightEffect(self, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, Filters.and(CardType.COMPANION, Filters.not(Culture.GONDOR, Race.MAN)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTwilightEffect(self, 4));
            return Collections.singletonList(action);
        }
        return null;
    }
}
