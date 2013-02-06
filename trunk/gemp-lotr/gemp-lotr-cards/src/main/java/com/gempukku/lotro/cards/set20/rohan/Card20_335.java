package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;

import java.util.Collection;

/**
 * 1
 * Muster the Rohirrim
 * Rohan	Event • Fellowship
 * Play a valiant [Rohan] man from hand to play a [Rohan] possession on that companion from your draw deck.
 */
public class Card20_335 extends AbstractEvent {
    public Card20_335() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Muster the Rohirrim", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.ROHAN, Race.MAN, Keyword.VALIANT);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.ROHAN, Race.MAN, Keyword.VALIANT) {
                    @Override
                    protected void afterCardPlayed(final PhysicalCard cardPlayed) {
                        if (!game.getModifiersQuerying().hasFlagActive(game.getGameState(), ModifierFlag.CANT_PLAY_FROM_DISCARD_OR_DECK))
                        action.appendEffect(
                                new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId),
                                        Filters.and(
                                                Culture.ROHAN, CardType.POSSESSION,
                                                ExtraFilters.attachableTo(game, cardPlayed)), 1, 1) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                        if (selectedCards.size() > 0) {
                                            PhysicalCard selectedCard = selectedCards.iterator().next();
                                            game.getActionsEnvironment().addActionToStack(((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, cardPlayed, 0));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
