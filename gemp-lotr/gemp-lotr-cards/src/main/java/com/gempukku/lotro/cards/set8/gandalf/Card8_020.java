package com.gempukku.lotro.cards.set8.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event • Fellowship
 * Game Text: Spot Gandalf and place a companion (except the Ring-bearer) in the dead pile to take 3 cards from that
 * companion’s culture into hand from your draw deck. Shuffle your draw deck.
 */
public class Card8_020 extends AbstractEvent {
    public Card8_020() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Saved From the Fire", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.insertCost(
                                new UnrespondableEffect() {
                                    @Override
                                    protected void doPlayEffect(LotroGame game) {
                                        game.getGameState().removeCardsFromZone(playerId, Collections.singleton(card));
                                        game.getGameState().addCardToZone(game, card, Zone.DEAD);
                                    }
                                });
                        action.appendEffect(
                                new ChooseArbitraryCardsEffect(playerId, "Choose cards to put in your hand", game.getGameState().getDeck(playerId), 0, 3) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                                        for (PhysicalCard selectedCard : selectedCards) {
                                            action.insertEffect(
                                                    new PutCardFromDeckIntoHandOrDiscardEffect(selectedCard));
                                        }
                                    }
                                });
                        action.appendEffect(
                                new ShuffleDeckEffect(playerId));
                    }
                });
        return action;
    }
}
