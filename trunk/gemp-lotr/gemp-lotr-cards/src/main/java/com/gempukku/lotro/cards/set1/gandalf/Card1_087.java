package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Play a [GANDALF] character from your draw deck.
 */
public class Card1_087 extends AbstractEvent {
    public Card1_087() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "A Wizard Is Never Late", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Filters.and(Filters.culture(Culture.GANDALF), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)))));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
