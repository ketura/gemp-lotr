package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Spot a [RAIDER] minion to add (2) for each threat.
 */
public class Card7_153 extends AbstractEvent {
    public Card7_153() {
        super(Side.SHADOW, 1, Culture.RAIDER, "Mumakil of the Harad", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Culture.RAIDER, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        int threats = game.getGameState().getThreats();
        action.appendEffect(
                new AddTwilightEffect(self, 2 * threats));
        return action;
    }
}
