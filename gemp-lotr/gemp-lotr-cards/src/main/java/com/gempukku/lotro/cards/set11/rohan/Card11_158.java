package com.gempukku.lotro.cards.set11.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.StackCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Response: If a [ROHAN] possession is discarded from play and no more than one possession is stacked here,
 * stack it here. Fellowship: Add (1) to play a possession stacked here as if from hand.
 */
public class Card11_158 extends AbstractPermanent {
    public Card11_158() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Sword Rack");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Culture.ROHAN, CardType.POSSESSION)
                && Filters.filter(game.getGameState().getStackedCards(self), game.getGameState(), game.getModifiersQuerying(), CardType.POSSESSION).size() <= 1) {
            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
            ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Stack " + GameUtils.getCardLink(discardResult.getDiscardedCard()));
            action.appendEffect(
                    new StackCardFromDiscardEffect(discardResult.getDiscardedCard(), self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromStacked(playerId, game, self, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, self, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
