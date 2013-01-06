package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Balin's Lament
 * Dwarven	Condition • Support Area
 * When you play this condition, stack the top card of your draw deck here.
 * Response: If the Shadow player is about to discard any mumber of your [Dwarven] conditions, discard this condition
 * to prevent that.
 */
public class Card20_038 extends AbstractPermanent {
    public Card20_038() {
        super(Side.FREE_PEOPLE, 4, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Balin's Lament", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new StackTopCardsFromDeckEffect(self, self.getOwner(), 1, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingDiscardedByOpponent(effect, game, playerId, Culture.DWARVEN, CardType.CONDITION)
                && PlayConditions.canSelfDiscard(self, game)) {
            DiscardCardsFromPlayEffect discardEffect = (DiscardCardsFromPlayEffect) effect;

            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new PreventCardEffect(discardEffect, Filters.filter(discardEffect.getAffectedCardsMinusPrevented(game),
                            game.getGameState(), game.getModifiersQuerying(), Culture.DWARVEN, CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
