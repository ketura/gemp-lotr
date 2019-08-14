package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CorruptRingBearerEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Maneuver: If the total number of burdens and companions in the dead pile is at least 12, spot
 * a [SAURON] Orc to corrupt the Ring-bearer.
 */
public class Card1_243 extends AbstractEvent {
    public Card1_243() {
        super(Side.SHADOW, 2, Culture.SAURON, "Despair", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.SAURON, Race.ORC)
                && (game.getGameState().getBurdens() + Filters.filter(game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()), game, CardType.COMPANION).size()) >= 12;
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new CorruptRingBearerEffect());
        return action;
    }
}
