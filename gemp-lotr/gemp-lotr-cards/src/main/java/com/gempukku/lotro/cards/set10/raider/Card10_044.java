package com.gempukku.lotro.cards.set10.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.PreventAllWoundsActionProxy;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Event • Shadow
 * Game Text: Until the end of the assignment phase, each time a Southron is about to take a wound, prevent that.
 */
public class Card10_044 extends AbstractEvent {
    public Card10_044() {
        super(Side.SHADOW, 1, Culture.RAIDER, "High Vantage", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddUntilEndOfPhaseActionProxyEffect(
                        new PreventAllWoundsActionProxy(self, Keyword.SOUTHRON), Phase.ASSIGNMENT));
        return action;
    }
}
