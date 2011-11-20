package com.gempukku.lotro.cards.set10.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 2
 * Type: Event • Fellowship
 * Game Text: For each [GONDOR] companion you spot, reveal 1 card at random from a Shadow player's hand. Choose
 * a revealed Shadow card to be discarded, then its owner draws a card.
 */
public class Card10_029 extends AbstractEvent {
    public Card10_029() {
        super(Side.FREE_PEOPLE, 2, Culture.GONDOR, "Drawing His Eye", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, Culture.GONDOR, CardType.COMPANION) {
                    @Override
                    protected void spottedCards(final int spotCount) {
                        action.appendEffect(
                                new ChooseOpponentEffect(playerId) {
                                    @Override
                                    protected void opponentChosen(final String opponentId) {
                                        action.appendEffect(
                                                new RevealRandomCardsFromHandEffect(playerId, opponentId, self, spotCount) {
                                                    @Override
                                                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                                        action.appendEffect(
                                                                new ChooseArbitraryCardsEffect(playerId, "Choose card to discard", revealedCards, Side.SHADOW, 1, 1) {
                                                                    @Override
                                                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                                                        for (PhysicalCard selectedCard : selectedCards) {
                                                                            action.appendEffect(
                                                                                    new DiscardCardsFromHandEffect(self, opponentId, Collections.singleton(selectedCard), true));
                                                                            action.appendEffect(
                                                                                    new DrawCardsEffect(opponentId, 1));
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });
                    }
                });
        return action;
    }
}
