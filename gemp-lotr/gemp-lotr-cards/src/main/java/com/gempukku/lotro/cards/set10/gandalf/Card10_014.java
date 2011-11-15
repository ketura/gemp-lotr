package com.gempukku.lotro.cards.set10.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDeadPileEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.*;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Spot your Wizard to exchange a companion in hand with a companion in your dead pile or discard pile.
 */
public class Card10_014 extends AbstractEvent {
    public Card10_014() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Borne Far Away", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Filters.owner(playerId), Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseCardsFromHandEffect(playerId, 1, 1, CardType.COMPANION) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                        for (final PhysicalCard cardInHand : selectedCards) {
                            List<Effect> possibleEffects = new LinkedList<Effect>();
                            possibleEffects.add(
                                    new ChooseCardsFromDiscardEffect(playerId, 1, 1, CardType.COMPANION) {
                                        @Override
                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                                            for (PhysicalCard cardInDiscard : cards) {
                                                Set<PhysicalCard> cardsToRemove = new HashSet<PhysicalCard>();
                                                cardsToRemove.add(cardInHand);
                                                cardsToRemove.add(cardInDiscard);

                                                game.getGameState().sendMessage(playerId + " exchanges " + GameUtils.getCardLink(cardInDiscard) + " from discard with " + GameUtils.getCardLink(cardInHand) + " in hand");
                                                game.getGameState().removeCardsFromZone(playerId, cardsToRemove);
                                                game.getGameState().addCardToZone(game, cardInDiscard, Zone.HAND);
                                                game.getGameState().addCardToZone(game, cardInHand, Zone.DISCARD);
                                            }
                                        }
                                    });
                            possibleEffects.add(
                                    new ChooseCardsFromDeadPileEffect(playerId, 1, 1, CardType.COMPANION) {
                                        @Override
                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                                            for (PhysicalCard cardInDeadPile : cards) {
                                                Set<PhysicalCard> cardsToRemove = new HashSet<PhysicalCard>();
                                                cardsToRemove.add(cardInHand);
                                                cardsToRemove.add(cardInDeadPile);

                                                game.getGameState().sendMessage(playerId + " exchanges " + GameUtils.getCardLink(cardInDeadPile) + " from dead pile with " + GameUtils.getCardLink(cardInHand) + " in hand");
                                                game.getGameState().removeCardsFromZone(playerId, cardsToRemove);
                                                game.getGameState().addCardToZone(game, cardInDeadPile, Zone.HAND);
                                                game.getGameState().addCardToZone(game, cardInHand, Zone.DEAD);
                                            }
                                        }
                                    });
                            action.appendEffect(
                                    new ChoiceEffect(action, playerId, possibleEffects));
                        }
                    }
                });
        return action;
    }
}
