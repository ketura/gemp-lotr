package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExhaustCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Event • Archery
 * Game Text: Wound 3 [MEN] archers to exhaust a companion.
 */
public class Card15_084 extends AbstractEvent {
    public Card15_084() {
        super(Side.SHADOW, 3, Culture.MEN, "Last Gasp", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canWound(self, game, 1, 3, Culture.MEN, Keyword.ARCHER);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndWoundCharactersEffect(action, playerId, 3, 3, Culture.MEN, Keyword.ARCHER));
        action.appendEffect(
                new ChooseAndExhaustCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
        return action;
    }
}
