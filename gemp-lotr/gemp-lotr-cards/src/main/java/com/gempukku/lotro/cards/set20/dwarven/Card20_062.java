package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 0
 * Miners of Mithril
 * Dwarven	Event • Fellowship
 * Stack a Free Peoples card from your hand on a [Dwarven] support area condition to draw a card.
 */
public class Card20_062 extends AbstractEvent {
    public Card20_062() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Miners of Mithril", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.hasCardInHand(game, self.getOwner(), 1, Filters.not(self), Side.FREE_PEOPLE)
                && PlayConditions.canSpot(game, Culture.DWARVEN, CardType.CONDITION, Keyword.SUPPORT_AREA);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a DWARVEN condition in your support area", Culture.DWARVEN, CardType.CONDITION, Keyword.SUPPORT_AREA) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendCost(
                                new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, card, Side.FREE_PEOPLE));
                    }
                });
        action.appendEffect(
                new DrawCardsEffect(action, playerId, 1));
        return action;
    }
}
