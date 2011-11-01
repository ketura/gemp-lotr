package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeadPileEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 5
 * Type: Event
 * Game Text: Fellowship: Spot an Elf, a [GONDOR] Man, and a Dwarf to play Gandalf from your dead pile.
 */
public class Card4_106 extends AbstractOldEvent {
    public Card4_106() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Well Met Indeed", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR), Filters.race(Race.MAN))
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF))
                && PlayConditions.canPlayFromDeadPile(playerId, game, Filters.gandalf);
    }

    @Override
    public int getTwilightCost() {
        return 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDeadPileEffect(playerId, game.getGameState().getDeadPile(playerId), Filters.gandalf));
        return action;
    }
}
