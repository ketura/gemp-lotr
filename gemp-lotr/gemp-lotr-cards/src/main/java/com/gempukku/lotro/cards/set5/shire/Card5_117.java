package com.gempukku.lotro.cards.set5.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canPlayFromDiscard(self.getOwner(), game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.smeagol));
        action.appendEffect(
                new HealCharactersEffect(self, self.getOwner(), Filters.and(Race.HOBBIT, Keyword.RING_BOUND)));
        action.appendEffect(
                new HealCharactersEffect(self, self.getOwner(), Filters.and(Race.HOBBIT, Keyword.RING_BOUND)));
        return action;
    }
}
