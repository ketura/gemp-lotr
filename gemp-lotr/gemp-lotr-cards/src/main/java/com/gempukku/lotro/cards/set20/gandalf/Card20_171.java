package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * A Wizard is Never Late
 * Gandalf	Event • Fellowship
 * Play a [Gandalf] character from your draw deck.
 */
public class Card20_171 extends AbstractEvent {
    public Card20_171() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "A Wizard is Never Late", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeckEffect(playerId, Culture.GANDALF, Filters.character));
        return action;
    }
}
