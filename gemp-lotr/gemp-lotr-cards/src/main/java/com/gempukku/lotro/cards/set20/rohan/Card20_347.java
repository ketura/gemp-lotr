package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * To Whatever End
 * Rohan	Event • Regroup
 * Exert a [Rohan] Man to play a [Rohan] fortification from your discard pile.
 */
public class Card20_347 extends AbstractEvent {
    public Card20_347() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "To Whatever End", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN)
                && PlayConditions.canPlayFromDiscard(self.getOwner(), game, Culture.ROHAN, Keyword.FORTIFICATION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, Race.MAN));
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ROHAN, Keyword.FORTIFICATION));
        return action;
    }
}
