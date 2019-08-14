package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;

import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Tale. Maneuver: Spot a [GONDOR] companion to reveal a card at random from an opponent's hand. Heal
 * X companions, where X is the twilight cost of the card revealed.
 */
public class Card3_043 extends AbstractEvent {
    public Card3_043() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Might of Numenor", Phase.MANEUVER);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.GONDOR, CardType.COMPANION);
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
                                            PhysicalCard revealedCard = revealedCards.get(0);
                                            int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                                            action.appendEffect(
                                                    new ChooseAndHealCharactersEffect(action, playerId, twilightCost, twilightCost, CardType.COMPANION));
                                        }
                                    }
                                });
                    }
                });
        return action;
    }
}
