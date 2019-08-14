package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Not A Defeat
 * Rohan	Event • Skirmish
 * Discard a [Rohan] fortification to cancel a skirmish involving a [Rohan] Man and an exhausted minion.
 */
public class Card20_474 extends AbstractEvent {
    public Card20_474() {
        super(Side.FREE_PEOPLE, 0, Culture.ROHAN, "Not a Defeat", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardFromPlay(self, game, Culture.ROHAN, Keyword.FORTIFICATION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ROHAN, Keyword.FORTIFICATION));
        action.appendEffect(
                new CancelSkirmishEffect(Culture.ROHAN, Race.MAN, Filters.inSkirmishAgainst(CardType.MINION, Filters.exhausted)));
        return action;
    }
}
