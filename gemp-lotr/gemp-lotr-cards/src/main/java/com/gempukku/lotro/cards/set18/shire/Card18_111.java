package com.gempukku.lotro.cards.set18.shire;

import com.gempukku.lotro.cards.AbstractFollower;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Follower
 * Resistance: +1
 * Game Text: Aid - (2). (At the start of the maneuver phase, you may add (2) to transfer this to a companion.)
 * Each time you attach this follower to a [SHIRE] companion, you may discard X cards from hand to draw X cards.
 */
public class Card18_111 extends AbstractFollower {
    public Card18_111() {
        super(Side.FREE_PEOPLE, 2, 0, 0, 1, Culture.SHIRE, "Robin Smallburrow", "Shirriff Cock-Robin", true);
    }

    @Override
    protected boolean canPayAidCost(LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    protected Effect getAidCost(LotroGame game, Action action, PhysicalCard self) {
        return new AddTwilightEffect(self, 2);
    }

    @Override
    protected List<OptionalTriggerAction> getExtraOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.transferredCard(game, effectResult, self, null, Filters.and(Culture.SHIRE, CardType.COMPANION))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 0, Integer.MAX_VALUE, Filters.any) {
                        @Override
                        protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                            if (cardsBeingDiscarded.size() > 0)
                                action.appendEffect(
                                        new DrawCardsEffect(action, playerId, cardsBeingDiscarded.size()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
