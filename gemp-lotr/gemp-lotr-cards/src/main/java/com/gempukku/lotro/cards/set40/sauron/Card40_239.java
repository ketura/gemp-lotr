package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;

/**
 * Title: Shadow in the East
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1C239
 * Game Text: Spot a [SAURON] minion to add a threat. Add an additional threat for each Free Peoples culture less than 4 you can spot.
 */
public class Card40_239 extends AbstractEvent {
    public Card40_239() {
        super(Side.SHADOW, 1, Culture.SAURON, "Shadow in the East", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        int fpCulturesCount = GameUtils.getSpottableFPCulturesCount(game.getGameState(), game.getModifiersQuerying(), playerId);
        int threatCount = 1 + Math.max(0, 4 - fpCulturesCount);
        action.appendEffect(
                new AddThreatsEffect(playerId, self, threatCount));
        return action;
    }
}
