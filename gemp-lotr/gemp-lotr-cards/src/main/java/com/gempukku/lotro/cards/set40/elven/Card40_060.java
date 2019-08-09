package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PutCardFromStackedOnTopOfDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Title: *The Mirror of Galadriel, Revealer of Portents
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Artifact - Support Area
 * Card Number: 1R60
 * Game Text: At the beginning of the fellowship phase, you may exert Galadriel to place a card stacked here on top
 * of your draw deck.
 * Regroup: Spot Galadriel to place an [ELVEN] card from hand here.
 */
public class Card40_060 extends AbstractPermanent{
    public Card40_060() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ELVEN, "The Mirror of Galadriel", "Revealer of Portents", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && PlayConditions.canExert(self, game, Filters.galadriel)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.galadriel));
            action.appendEffect(
                    new ChooseStackedCardsEffect(action, playerId, 1, 1, self, Filters.any) {
                        @Override
                        protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
                            for (PhysicalCard stackedCard : stackedCards) {
                                action.appendEffect(
                                        new PutCardFromStackedOnTopOfDeckEffect(stackedCard));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canSpot(game, Filters.galadriel)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.ELVEN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
