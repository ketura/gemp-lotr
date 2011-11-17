package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Spot a Dwarf to take a Free Peoples card stacked on a [DWARVEN] condition into hand.
 */
public class Card4_045 extends AbstractOldEvent {
    public Card4_045() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Dwarven Foresight", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.side(Side.FREE_PEOPLE)) {
                    @Override
                    protected void cardsChosen(Collection<PhysicalCard> stackedCards) {
                        for (PhysicalCard card : stackedCards)
                            action.insertEffect(
                                    new PutCardFromStackedIntoHandEffect(card));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
