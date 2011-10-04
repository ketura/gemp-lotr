package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.cards.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;

import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Reveal a card at random from an opponent's hand. Heal X [ELVEN] allies, where X is
 * the twilight cost of the card revealed.
 */
public class Card3_016 extends AbstractEvent {
    public Card3_016() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Friends of Old", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        action.insertEffect(
                                new RevealRandomCardsFromHandEffect(opponentId, 1) {
                                    @Override
                                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                        if (revealedCards.size() > 0) {
                                            PhysicalCard revealedCard = revealedCards.get(0);
                                            int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                                            action.appendEffect(
                                                    new ChooseAndHealCharactersEffect(action, playerId, twilightCost, twilightCost, Filters.culture(Culture.ELVEN), Filters.type(CardType.ALLY)));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
