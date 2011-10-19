package com.gempukku.lotro.cards.set6.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: While this card is stacked on a [DWARVEN] condition, Gimli is strength +1. Fellowship: Place this card
 * or another [DWARVEN] card from hand on top of or beneath your draw deck.
 */
public class Card6_011 extends AbstractEvent {
    public Card6_011() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Toss Me", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return null;
    }
}
