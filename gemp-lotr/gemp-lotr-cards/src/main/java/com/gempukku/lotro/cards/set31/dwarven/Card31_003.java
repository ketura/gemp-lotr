package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Artifact • Support Area
 * Game Text: Fellowship: Add (1) to take a card stacked here into hand. Regroup: Exert an ally to stack a [DWARVEN]
 * event from hand on Emeralds of Girion.
 */
public class Card31_003 extends AbstractPermanent {
    public Card31_003() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.DWARVEN, "Emeralds of Girion", null, true);
    }


    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(self);
            if (stackedCards.size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new AddTwilightEffect(self, 1));
                action.appendEffect(
                        new ChooseArbitraryCardsEffect(playerId, "Choose card", stackedCards, 1, 1) {
                            @Override
                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                for (PhysicalCard selectedCard : selectedCards) {
                                    action.appendEffect(
                                            new PutCardFromStackedIntoHandEffect(selectedCard));
                                }
                            }
                        });
                return Collections.singletonList(action);
            }
        }

        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
            && PlayConditions.canExert(self, game, CardType.ALLY)) {
            if (Filters.filter(game.getGameState().getHand(playerId), game,
                    Culture.DWARVEN, CardType.EVENT).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.ALLY));
                action.appendEffect(
                        new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.DWARVEN));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
