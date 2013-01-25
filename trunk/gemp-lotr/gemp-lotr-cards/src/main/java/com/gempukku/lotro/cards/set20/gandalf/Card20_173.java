package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Wizard's Forethought
 * Gandalf	Condition • Support Area
 * When there are 3 cards stacked here, discard this condition.
 * Fellowship: Spot Gandalf and add (1) to stack a Free Peoples card here from hand.
 * Maneuver: Exert Gandalf and discard this condition to draw X cards, where X is the number of Free Peoples cards stacked here.
 */
public class Card20_173 extends AbstractPermanent {
    public Card20_173() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.GANDALF, Zone.SUPPORT, "Wizard's Forethought", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getGameState().getStackedCards(self).size()>=3) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSpot(game, Filters.gandalf)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 1));
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Side.FREE_PEOPLE));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, Filters.gandalf)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, Filters.filter(game.getGameState().getStackedCards(self), game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE).size()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
