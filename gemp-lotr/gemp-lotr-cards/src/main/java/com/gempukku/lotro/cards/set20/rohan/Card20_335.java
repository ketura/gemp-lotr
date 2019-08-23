package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.timing.ExtraFilters;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canPlayFromHand(self.getOwner(), game, Culture.ROHAN, Race.MAN, Keyword.VALIANT);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.ROHAN, Race.MAN, Keyword.VALIANT) {
                    @Override
                    protected void afterCardPlayed(final PhysicalCard cardPlayed) {
                        if (!game.getModifiersQuerying().hasFlagActive(game, ModifierFlag.CANT_PLAY_FROM_DISCARD_OR_DECK))
                        action.appendEffect(
                                new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getDeck(playerId),
                                        Filters.and(
                                                Culture.ROHAN, CardType.POSSESSION,
                                                ExtraFilters.attachableTo(game, cardPlayed)), 1, 1) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                        if (selectedCards.size() > 0) {
                                            PhysicalCard selectedCard = selectedCards.iterator().next();
                                            game.getActionsEnvironment().addActionToStack(PlayUtils.getPlayCardAction(game, selectedCard, 0, cardPlayed, false));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
