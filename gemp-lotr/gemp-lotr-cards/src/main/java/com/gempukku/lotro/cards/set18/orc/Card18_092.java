package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Remove a threat to reinforce an [ORC] token.
 */
public class Card18_092 extends AbstractEvent {
    public Card18_092() {
        super(Side.SHADOW, 0, Culture.ORC, "War Preparations", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canRemoveThreat(game, self, 1);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new RemoveThreatsEffect(self, 1));
        action.appendEffect(
                new ReinforceTokenEffect(self, playerId, Token.ORC));
        return action;
    }
}
