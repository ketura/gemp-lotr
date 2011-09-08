package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert X companions to remove (X).
 */
public class Card1_077 extends AbstractEvent {
    public Card1_077() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Let Folly Be Our Cloak", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardsEffect(playerId, "Choose companions", 0, Integer.MAX_VALUE, Filters.type(CardType.COMPANION), Filters.canExert()) {
                    @Override
                    protected void cardsSelected(List<PhysicalCard> cards) {
                        for (PhysicalCard card : cards)
                            action.addCost(new ExertCharacterEffect(card));
                        action.addEffect(new RemoveTwilightEffect(cards.size()));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
