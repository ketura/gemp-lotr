package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * Festive Folk
 * Dwarven	Event • Fellowship
 * Exert a Dwarf and discard a card stacked on a [Dwarven] condition to remove a burden.
 */
public class Card20_051 extends AbstractEvent {
    public Card20_051() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Festive Folk", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Race.DWARF)
                && PlayConditions.canSpot(game, Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Filters.any));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF));
        action.appendCost(
                new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Filters.any));
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self, 1));
        return action;
    }
}
