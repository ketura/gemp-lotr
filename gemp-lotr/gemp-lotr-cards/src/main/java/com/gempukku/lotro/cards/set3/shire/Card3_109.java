package com.gempukku.lotro.cards.set3.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;

import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Spot a Hobbit companion (except the Ring-bearer) to reveal a card at random from
 * an opponent's hand. Remove (X), where X is the twilight cost of the card revealed.
 */
public class Card3_109 extends AbstractEvent {
    public Card3_109() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Meant to Be Alone", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.HOBBIT, CardType.COMPANION, Filters.not(Filters.ringBearer));
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
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
                                            PhysicalCard revealedCard = revealedCards.get(0);
                                            int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                                            action.appendEffect(
                                                    new RemoveTwilightEffect(twilightCost));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
