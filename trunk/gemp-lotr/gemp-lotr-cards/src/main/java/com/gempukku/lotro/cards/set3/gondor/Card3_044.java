package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.*;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Artifact
 * Game Text: Plays to your support area. Fellowship: Stack a [GONDOR] card from hand here. Fellowship: Add (1) to take
 * a card stacked here into hand.
 */
public class Card3_044 extends AbstractPermanent {
    public Card3_044() {
        super(Side.FREE_PEOPLE, 1, CardType.ARTIFACT, Culture.GONDOR, Zone.FREE_SUPPORT, "The Shards of Narsil", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            if (Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR)).size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Stack a GONDOR card from hand here";
                    }
                };
                action.appendEffect(
                        new ChooseCardsFromHandEffect(playerId, 1, 1, Filters.culture(Culture.GONDOR)) {
                            @Override
                            protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                for (PhysicalCard selectedCard : selectedCards) {
                                    action.appendEffect(
                                            new StackCardFromHandEffect(selectedCard, self));
                                }
                            }
                        }
                );
                actions.add(action);
            }

            List<PhysicalCard> stackedCards = game.getGameState().getStackedCards(self);
            if (stackedCards.size() > 0) {
                final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Add (1) to take card stacked here into hand";
                    }
                };
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
                actions.add(action);
            }

        }

        return actions;
    }
}
