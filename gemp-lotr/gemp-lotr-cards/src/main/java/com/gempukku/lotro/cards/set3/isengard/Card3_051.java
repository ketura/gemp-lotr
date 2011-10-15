package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
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
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Regroup: Reveal a card at random from the Free Peoples player's hand. Heal X [ISENGARD] minions, where X
 * is the twilight cost of the card revealed.
 */
public class Card3_051 extends AbstractOldEvent {
    public Card3_051() {
        super(Side.FREE_PEOPLE, Culture.ISENGARD, "Coming for the Ring", Phase.REGROUP);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealRandomCardsFromHandEffect(game.getGameState().getCurrentPlayerId(), 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        if (revealedCards.size() > 0) {
                            PhysicalCard revealedCard = revealedCards.get(0);
                            int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                            action.appendEffect(
                                    new ChooseAndHealCharactersEffect(action, playerId, twilightCost, twilightCost, Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION)));
                        }
                    }
                });
        return action;
    }
}
