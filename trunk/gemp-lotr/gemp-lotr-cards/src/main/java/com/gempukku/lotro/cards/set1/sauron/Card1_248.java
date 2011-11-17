package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Spot X [SAURON] minions to add (X).
 */
public class Card1_248 extends AbstractOldEvent {
    public Card1_248() {
        super(Side.SHADOW, Culture.SAURON, "Forces of Mordor", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        // TODO This should give an option to spot less
        int sauronMinions = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.SAURON, CardType.MINION);
        action.appendEffect(
                new AddTwilightEffect(self, sauronMinions));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
