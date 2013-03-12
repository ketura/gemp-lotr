package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.cards.effects.StackPlayedEventOnACardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.cards.results.PlayEventResult;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 0
 * •The Palantir of Orthanc, Defiler of Wizards
 * Isengard	Artifact • Support Area
 * Response: If a spell is played, spot Saruman to stack that spell here.
 * Shadow: Spot Saruman and remove a threat to take an [Isengard] spell stacked here into hand.
 */
public class Card20_239 extends AbstractPermanent {
    public Card20_239() {
        super(Side.SHADOW, 0, CardType.ARTIFACT, Culture.ISENGARD, Zone.SUPPORT, "The Palantir of Orthanc", "Defiler of Wizards", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ISENGARD, CardType.MINION);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Keyword.SPELL)
                && PlayConditions.canSpot(game, Filters.saruman)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new StackPlayedEventOnACardEffect(((PlayEventResult) effectResult).getPlayEventAction(), self));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, Filters.saruman)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseStackedCardsEffect(action, playerId, 1, 1, self, Filters.and(Culture.ISENGARD, Keyword.SPELL)) {
                        @Override
                        protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
                            for (PhysicalCard stackedCard : stackedCards) {
                                action.appendEffect(
                                        new PutCardFromStackedIntoHandEffect(stackedCard));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
