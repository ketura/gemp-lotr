package com.gempukku.lotro.cards.set40.dwarven;

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
 * Title: Miners of Mithril
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event - Fellowship
 * Card Number: 1C24
 * Game Text: Stack a Free Peoples card from hand on a [DWARVEN] support area condition to draw a card.
 */
public class Card40_024 extends AbstractEvent {
    public Card40_024() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Miners of Mithril", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canStackCardFromHand(self, game, self.getOwner(), 1, Filters.and(Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT), Side.FREE_PEOPLE);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose a condition to stack on", Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendCost(
                                new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, card,
                                        Side.FREE_PEOPLE));
                    }
                });
        action.appendEffect(
                new DrawCardsEffect(action, playerId, 1));
        return action;
    }
}
