package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.*;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Fellowship: Add a burden to choose 2 [SHIRE] events with different card titles from your discard pile.
 * Choose an opponent and make him or her choose 1 of those cards for you to take into hand.
 */
public class Card10_104 extends AbstractPermanent {
    public Card10_104() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Birthday Present");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(self, 1));
            Collection<PhysicalCard> shireEvents = Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.SHIRE, CardType.EVENT);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose events", filterUniqueNames(shireEvents), 2, 2) {
                        @Override
                        protected void cardsSelected(LotroGame game, final Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                action.appendEffect(
                                        new ChooseOpponentEffect(playerId) {
                                            @Override
                                            protected void opponentChosen(String opponentId) {
                                                action.appendEffect(
                                                        new ChooseArbitraryCardsEffect(opponentId, "Choose event Free People player should take into hand", selectedCards, 1, 1) {
                                                            @Override
                                                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                                for (PhysicalCard selectedCard : selectedCards) {
                                                                    action.appendEffect(
                                                                            new PutCardFromDiscardIntoHandEffect(selectedCard));
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    private Collection<PhysicalCard> filterUniqueNames(Collection<PhysicalCard> cards) {
        Map<String, PhysicalCard> nameMap = new HashMap<String, PhysicalCard>();
        for (PhysicalCard card : cards)
            nameMap.put(card.getBlueprint().getName(), card);
        return nameMap.values();
    }
}
