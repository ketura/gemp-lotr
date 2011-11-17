package com.gempukku.lotro.cards.set5.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Play Smeagol from your discard pile to heal each Ring-bound Hobbit twice.
 */
public class Card5_117 extends AbstractEvent {
    public Card5_117() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "You Must Help Us", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canPlayFromDiscard(playerId, game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.smeagol));
        action.appendEffect(
                new HealCharactersEffect(self, Filters.and(Race.HOBBIT, Keyword.RING_BOUND)));
        action.appendEffect(
                new HealCharactersEffect(self, Filters.and(Race.HOBBIT, Keyword.RING_BOUND)));
        return action;
    }
}
