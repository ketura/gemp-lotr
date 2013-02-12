package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 0
 * Well Earned Comforts
 * Shire	Event • Fellowship
 * Exert a Hobbit to remove a burden (or 2 burdens if you can spot 4 pipes).
 */
public class Card20_415 extends AbstractEvent {
    public Card20_415() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Well Earned Comforts", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.HOBBIT);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT));
        int count = PlayConditions.canSpot(game, 4, PossessionClass.PIPE)?2:1;
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self, count));
        return action;
    }
}
