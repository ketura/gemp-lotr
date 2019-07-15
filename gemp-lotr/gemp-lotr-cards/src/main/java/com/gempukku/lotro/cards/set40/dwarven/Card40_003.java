package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PreventEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Balin's Lament
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1U3
 * Game Text: Tale. When you play this condition, stack the top card of your draw deck here.
 * Response: If a Shadow card is about to discard any number of your [DWARVEN] conditions, discard this condition to prevent that.
 */
public class Card40_003 extends AbstractPermanent {
    public Card40_003(Side side, int twilightCost, CardType cardType, Culture culture, Zone playedToZone, String name) {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Balin's Lament", null, true);
        addKeyword(Keyword.TALE);
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
        if (TriggerConditions.isGettingDiscardedBy(effect, game, Side.SHADOW, Filters.and(Culture.DWARVEN, CardType.CONDITION))
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(
                    new PreventCardEffect((DiscardCardsFromPlayEffect) effect, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
