package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Event • Maneuver
 * Game Text: Spot a [RAIDER] minion to add a threat. Add an additional threat for each companion over 4.
 */
public class Card7_173 extends AbstractEvent {
    public Card7_173() {
        super(Side.SHADOW, 1, Culture.RAIDER, "War Towers", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.RAIDER, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        int companionCount = Filters.countActive(game, CardType.COMPANION);
        int threats = 1 + (Math.max(0, companionCount - 4));
        action.appendEffect(
                new AddThreatsEffect(playerId, self, threats));
        return action;
    }
}
