package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.HOBBIT);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT));
        int count = PlayConditions.canSpot(game, 4, PossessionClass.PIPE)?2:1;
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self, count));
        return action;
    }
}
