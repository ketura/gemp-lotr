package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Spell. Fellowship: Spot Gandalf to discard all conditions.
 */
public class Card4_092 extends AbstractEvent {
    public Card4_092() {
        super(Side.FREE_PEOPLE, 5, Culture.GANDALF, "Grown Suddenly Tall", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new DiscardCardsFromPlayEffect(self.getOwner(), self, CardType.CONDITION));
        return action;
    }
}
