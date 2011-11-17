package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.RevealCardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;

import java.util.Collection;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Regroup: Spot Saruman or an [ISENGARD] Man, reveal your hand, and discard all Free Peoples cards revealed
 * to take an [ISENGARD] card into hand from your discard pile.
 */
public class Card4_162 extends AbstractOldEvent {
    public Card4_162() {
        super(Side.SHADOW, Culture.ISENGARD, "New Power Rising", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Filters.or(Filters.saruman, Filters.and(Culture.ISENGARD, Race.MAN)));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new RevealCardEffect(self, Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.not(self))));
        action.appendCost(
                new DiscardCardsFromHandEffect(self, playerId, Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE), false));
        action.appendEffect(
                new ChooseCardsFromDiscardEffect(playerId, 1, 1, Culture.ISENGARD) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        for (PhysicalCard card : cards) {
                            action.appendEffect(
                                    new PutCardFromDiscardIntoHandEffect(card));
                        }
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
