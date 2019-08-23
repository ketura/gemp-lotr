package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Title: Festive Folk
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event - Fellowship
 * Card Number: 1C16
 * Game Text: Exert a Dwarf and discard a card stacked on [DWARVEN] condition to remove a burden.
 */
public class Card40_016 extends AbstractEvent {
    public Card40_016() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Festive Folk", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.DWARF)
                && PlayConditions.canDiscardFromStacked(self, game, self.getOwner(), 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF));
        action.appendCost(
                new ChooseAndDiscardStackedCardsEffect(action ,playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self));
        return action;
    }
}
