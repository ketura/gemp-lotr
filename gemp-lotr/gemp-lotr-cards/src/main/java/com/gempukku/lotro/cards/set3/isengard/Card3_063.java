package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an [ISENGARD] minion to make the Free Peoples player exert X companions, where X is
 * the number of these races you can spot in the fellowship: Dwarf, Elf, Man, and Wizard.
 */
public class Card3_063 extends AbstractEvent {
    public Card3_063() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "One of You Must Do This", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.ISENGARD, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ISENGARD, CardType.MINION));
        int exertionCount = 0;
        if (Filters.canSpot(game, CardType.COMPANION, Race.DWARF))
            exertionCount++;
        if (Filters.canSpot(game, CardType.COMPANION, Race.ELF))
            exertionCount++;
        if (Filters.canSpot(game, CardType.COMPANION, Race.MAN))
            exertionCount++;
        if (Filters.canSpot(game, CardType.COMPANION, Race.WIZARD))
            exertionCount++;
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), exertionCount, exertionCount, CardType.COMPANION));
        return action;
    }
}
