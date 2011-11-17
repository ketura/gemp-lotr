package com.gempukku.lotro.cards.set4.dwarven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Spot a Dwarf companion and an Elf companion to wound a minion.
 */
public class Card4_053 extends AbstractOldEvent {
    public Card4_053() {
        super(Side.FREE_PEOPLE, Culture.DWARVEN, "Quick As May Be", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF), CardType.COMPANION)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), CardType.COMPANION);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
        return action;
    }
}
