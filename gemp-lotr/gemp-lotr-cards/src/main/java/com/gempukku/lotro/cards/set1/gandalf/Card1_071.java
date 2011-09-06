package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Fellowship: Spot Gandalf, a Dwarf, and an Elf to make the move limit for this turn +1.
 */
public class Card1_071 extends AbstractEvent {
    public Card1_071() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Durin's Secret", Phase.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new AddUntilEndOfTurnModifierEffect(
                        new MoveLimitModifier(self, 1)));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.name("Gandalf"))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF));
    }
}
