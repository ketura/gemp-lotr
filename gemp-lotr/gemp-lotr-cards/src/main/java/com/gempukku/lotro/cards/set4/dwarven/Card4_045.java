package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseStackedCardsEffect;

import java.util.Collection;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Spot a Dwarf to take a Free Peoples card stacked on a [DWARVEN] condition into hand.
 */
public class Card4_045 extends AbstractEvent {
    public Card4_045() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Dwarven Foresight", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Side.FREE_PEOPLE) {
                    @Override
                    protected void cardsChosen(LotroGame game, Collection<PhysicalCard> stackedCards) {
                        for (PhysicalCard card : stackedCards)
                            action.insertEffect(
                                    new PutCardFromStackedIntoHandEffect(card));
                    }
                });
        return action;
    }
}
