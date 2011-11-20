package com.gempukku.lotro.cards.set9.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.StackTopCardsFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Shadow: Play an [ISENGARD] minion to stack the top card of your draw deck on this card.
 * Skirmish: Remove (1) and discard a Free Peoples card stacked here to make an [ISENGARD] minion strength +1.
 * Regroup: Remove (1) to take an [ISENGARD] card stacked here into hand.
 */
public class Card9_039 extends AbstractPermanent {
    public Card9_039() {
        super(Side.SHADOW, 2, CardType.ARTIFACT, Culture.ISENGARD, Zone.SUPPORT, "Library of Orthanc", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.ISENGARD, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.ISENGARD, CardType.MINION));
            action.appendEffect(
                    new StackTopCardsFromDeckEffect(self, playerId, 1, self));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 1)
                && Filters.filter(game.getGameState().getStackedCards(self), game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, self, Side.FREE_PEOPLE));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Culture.ISENGARD, CardType.MINION));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 1)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseStackedCardsEffect(action, playerId, 1, 1, self, Culture.ISENGARD) {
                        @Override
                        protected void cardsChosen(Collection<PhysicalCard> stackedCards) {
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
