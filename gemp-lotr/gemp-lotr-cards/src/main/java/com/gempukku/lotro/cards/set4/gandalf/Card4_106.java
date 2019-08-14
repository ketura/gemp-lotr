package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeadPileEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Fellowship: Spot an Elf, a [GONDOR] Man, and a Dwarf to play Gandalf from your dead pile.
 */
public class Card4_106 extends AbstractEvent {
    public Card4_106() {
        super(Side.FREE_PEOPLE, 5, Culture.GANDALF, "Well Met Indeed", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Race.ELF)
                && Filters.canSpot(game, Culture.GONDOR, Race.MAN)
                && Filters.canSpot(game, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeadPileEffect(playerId, game, Filters.gandalf));
        return action;
    }
}
