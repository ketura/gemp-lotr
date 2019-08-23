package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 3
 * Arrows of the North
 * Gondor	Event • Archery
 * Exert 2 [Gondor] rangers to wound each roaming minion.
 */
public class Card20_180 extends AbstractEvent {
    public Card20_180() {
        super(Side.FREE_PEOPLE, 3, Culture.GONDOR, "Arrows of the North", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, 1, 2, Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, 1, Culture.GONDOR, Keyword.RANGER));
        action.appendEffect(
                new WoundCharactersEffect(self, CardType.MINION, Keyword.ROAMING));
        return action;
    }
}
