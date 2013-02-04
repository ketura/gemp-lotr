package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;

/**
 * 0
 * War Drums
 * Moria	Event • Shadow
 * Spot a Goblin stacked on a [Moria] condition to add (1) for each possession you can spot.
 */
public class Card20_282 extends AbstractEvent {
    public Card20_282() {
        super(Side.SHADOW, 0, Culture.MORIA, "War Drums", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.isActive(game, Culture.MORIA, CardType.CONDITION, Filters.hasStacked(Race.GOBLIN));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddTwilightEffect(self,
                        new CountSpottableEvaluator(CardType.POSSESSION)));
        return action;
    }
}
