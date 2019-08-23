package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Event • Maneuver
 * Game Text: Toil 2. (For each [ORC] character you exert when playing this, its twilight cost is -2) Spot an [ORC]
 * minion to make the Free Peoples player discard one of his or her conditions from play.
 */
public class Card12_103 extends AbstractEvent {
    public Card12_103() {
        super(Side.SHADOW, 3, Culture.ORC, "Storming the Ramparts", Phase.MANEUVER);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.CONDITION, Filters.owner(game.getGameState().getCurrentPlayerId())));
        return action;
    }
}
