package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Awoken in the Darkness
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Condition - Support Area
 * Card Number: 1C154
 * Game Text: When you play this condition, reveal the top 6 cards of your draw deck and stack them here.
 * Shadow: Remove (2) to play a Goblin stacked here.
 */
public class Card40_154 extends AbstractPermanent {
    public Card40_154() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, "Awoken in the Darkness", null,true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new StackTopCardsFromDeckEffect(self, self.getOwner(), 6, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 2)
        && PlayConditions.canPlayFromStacked(playerId, game, 2, self, Race.GOBLIN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
