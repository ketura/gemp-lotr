package com.gempukku.lotro.cards.set12.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.PlayPermanentAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.RevealTopCardsOfDrawDeckEffect;

import java.util.Collection;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Spot X [GOLLUM] cards to reveal the top X cards of your draw deck. You may play a revealed minion. Its
 * twilight cost is -2.
 */
public class Card12_038 extends AbstractEvent {
    public Card12_038() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "From Deep in Shadow", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        int count = Filters.countActive(game, Culture.GOLLUM);
        action.appendEffect(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, count) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        action.appendEffect(
                                new ChooseArbitraryCardsEffect(playerId, "Choose minion to play", revealedCards, Filters.and(CardType.MINION, Filters.playable(game, -2)), 0, 1) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                        if (selectedCards.size() > 0) {
                                            final PhysicalCard selectedCard = selectedCards.iterator().next();
                                            PlayPermanentAction action = (PlayPermanentAction) PlayUtils.getPlayCardAction(game, selectedCard, -2, Filters.any, false);
                                            action.skipShufflingDeck();
                                            game.getActionsEnvironment().addActionToStack(action);
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
