package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromStackedEffect;

/**
 * 0
 * Host of Goblins
 * Moria	Event • Shadow
 * Play a Goblin stacked on a [Moria] condition as if from hand.
 */
public class Card20_276 extends AbstractEvent {
    public Card20_276() {
        super(Side.SHADOW, 0, Culture.MORIA, "Host of Goblins", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN));
        return action;
    }
}
