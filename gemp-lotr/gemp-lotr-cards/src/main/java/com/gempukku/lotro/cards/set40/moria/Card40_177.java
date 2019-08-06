package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Title: Host of Goblins
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event - Shadow
 * Card Number: 1C177
 * Game Text: Play a Goblin stacked on a [MORIA] condition.
 */
public class Card40_177 extends AbstractEvent {
    public Card40_177() {
        super(Side.SHADOW, 0, Culture.MORIA, "Host of Goblins", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canPlayFromStacked(playerId, game, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN));
        return action;
    }
}
