package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Play a [DWARVEN] follower from your discard pile.
 */
public class Card30_020 extends AbstractEvent {
    public Card30_020() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "To me! O My Kinsfolk!", Phase.FELLOWSHIP);
    }


    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
				new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.DWARVEN, CardType.FOLLOWER));
		return action;
    }
}