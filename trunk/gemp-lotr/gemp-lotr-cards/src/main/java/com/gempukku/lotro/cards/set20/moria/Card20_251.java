package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
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
 * 3
 * •Awoken in Darkness
 * Moria	Condition • Support Area
 * When you play this condition, reveal the top 6 cards of your draw deck and stack them here.
 * Shadow: Remove (2) to play a Goblin stacked here.
 */
public class Card20_251 extends AbstractPermanent {
    public Card20_251() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Awoken in Darkness", null, true);
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
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 2)) {
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
