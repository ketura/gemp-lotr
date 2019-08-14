package com.gempukku.lotro.cards.set3.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Reveal a card at random from an opponent's hand. Shuffle up to X [DWARVEN] cards from your
 * discard pile into your draw deck, where X is the twilight cost of the card revealed.
 */
public class Card3_006 extends AbstractEvent {
    public Card3_006() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Storm of Argument", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.insertEffect(
                                new RevealRandomCardsFromHandEffect(playerId, opponentId, self, 1) {
                                    @Override
                                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                        if (revealedCards.size() > 0) {
                                            PhysicalCard card = revealedCards.get(0);
                                            int twilightCost = card.getBlueprint().getTwilightCost();
                                            action.insertEffect(
                                                    new ChooseCardsFromDiscardEffect(playerId, 0, twilightCost, Culture.DWARVEN) {
                                                        @Override
                                                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                                                            action.insertEffect(
                                                                    new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, cards));
                                                        }
                                                    }
                                            );
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
