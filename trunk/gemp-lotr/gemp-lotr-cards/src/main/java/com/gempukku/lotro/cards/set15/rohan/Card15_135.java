package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.LiberateASiteEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Follower
 * Game Text: Aid - (1). Each time you transfer this to a companion, except a [ROHAN] companion, exert bearer twice.
 * Each time bearer wins a skirmish, you may liberate a site.
 */
public class Card15_135 extends AbstractFollower {
    public Card15_135() {
        super(Side.FREE_PEOPLE, 1, 0, 0, 0, Culture.ROHAN, "Rohan Worker");
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, PhysicalCard self) {
        return new AddTwilightEffect(self, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, Filters.and(CardType.COMPANION, Filters.not(Culture.ROHAN)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.hasAttached(self)));
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<OptionalTriggerAction> getExtraOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new LiberateASiteEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
