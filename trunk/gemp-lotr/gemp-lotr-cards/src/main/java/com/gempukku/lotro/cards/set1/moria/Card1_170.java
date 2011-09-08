package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Spot a Hobbit (except the Ring-bearer) to reveal cards from the top of your draw deck until you
 * reveal one that is not a [MORIA] minion. Take the revealed cards into hand.
 */
public class Card1_170 extends AbstractEvent {
    public Card1_170() {
        super(Side.SHADOW, Culture.MORIA, "Fool of a Took!", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.HOBBIT), Filters.not(Filters.keyword(Keyword.RING_BEARER)));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new UnrespondableEffect() {
                    @Override
                    public void playEffect(LotroGame game) {
                        List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                        List<PhysicalCard> cardsToPutIntoHand = new LinkedList<PhysicalCard>();
                        for (PhysicalCard physicalCard : deck) {
                            cardsToPutIntoHand.add(physicalCard);
                            LotroCardBlueprint blueprint = physicalCard.getBlueprint();
                            if (blueprint.getCulture() != Culture.MORIA || blueprint.getCardType() != CardType.MINION)
                                break;
                        }
                        for (PhysicalCard cardToPutIntoHand : cardsToPutIntoHand) {
                            action.addEffect(
                                    new PutCardFromDeckIntoHandOrDiscardEffect(cardToPutIntoHand));
                        }
                    }
                });

        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
