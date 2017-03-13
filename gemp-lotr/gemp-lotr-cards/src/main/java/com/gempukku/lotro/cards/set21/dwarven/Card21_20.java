package com.gempukku.lotro.cards.set21.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Play a [DWARVEN] follower from your discard pile.
 */
public class Card21_20 extends AbstractEvent {
    public Card21_20() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "To me! O My Kinsfolk!", Phase.FELLOWSHIP);
    }


    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
				new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.DWARVEN, CardType.FOLLOWER));
		return Collections.singletonList(action);
    }
}