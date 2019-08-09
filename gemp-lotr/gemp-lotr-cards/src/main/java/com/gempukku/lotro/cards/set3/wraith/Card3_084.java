package com.gempukku.lotro.cards.set3.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.RevealRandomCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: Spot a Nazgul to reveal a card at random from the Free Peoples player's hand. Exert a companion
 * bearing a ranged weapon X times, where X is the twilight cost of the card revealed.
 */
public class Card3_084 extends AbstractEvent {
    public Card3_084() {
        super(Side.SHADOW, 2, Culture.WRAITH, "They Will Never Stop Hunting You", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RevealRandomCardsFromHandEffect(playerId, game.getGameState().getCurrentPlayerId(), self, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        if (revealedCards.size() > 0) {
                            PhysicalCard revealedCard = revealedCards.get(0);
                            final int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                            if (twilightCost > 0) {
                                action.appendEffect(
                                        new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION, Filters.hasAttached(PossessionClass.RANGED_WEAPON)) {
                                            @Override
                                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                for (int i = 0; i < twilightCost; i++)
                                                    action.appendEffect(
                                                            new ExertCharactersEffect(action, self, card));
                                            }
                                        });
                            }
                        }
                    }
                });
        return action;
    }
}
