package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Response: If a [MEN] possession is discarded from play, stack it here. Shadow: Remove (1) to play
 * a possession stacked here as if from hand.
 */
public class Card12_058 extends AbstractPermanent {
    public Card12_058() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Countless Companies");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Culture.MEN, CardType.POSSESSION)) {
            DiscardCardsFromPlayResult result = (DiscardCardsFromPlayResult) effectResult;
            final PhysicalCard discardedCard = result.getDiscardedCard();
            ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Stack " + GameUtils.getCardLink(discardedCard));
            action.appendEffect(
                    new StackCardFromDiscardEffect(discardedCard, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 1)
                && PlayConditions.canPlayFromStacked(playerId, game, 1, self, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
