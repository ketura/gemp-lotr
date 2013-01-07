package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.List;

/**
 * 2
 * Galadriel's Wisdom
 * Elven	Event • Regroup
 * Exert Galadriel twice to reveal the top card of your draw deck. Discard X Shadow conditions, where X is the revealed card's twilight cost.
 */
public class Card20_085 extends AbstractEvent {
    public Card20_085() {
        super(Side.FREE_PEOPLE, 2, Culture.ELVEN, "Galadriel's Wisdom", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 2, 1, Filters.galadriel);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.galadriel));
        action.appendEffect(
                new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                    @Override
                    protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                        for (PhysicalCard revealedCard : revealedCards) {
                            int twilightCost = revealedCard.getBlueprint().getTwilightCost();
                            action.appendEffect(
                                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, twilightCost, twilightCost, CardType.CONDITION, Side.SHADOW));
                        }
                    }
                });
        return action;
    }
}
