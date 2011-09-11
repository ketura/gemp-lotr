package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
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
        super(Side.SHADOW, Culture.SAURON, "Despair", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC))
                && (game.getGameState().getBurdens() + Filters.filter(game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()), game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)).size()) >= 12;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new CorruptRingBearerEffect());
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
