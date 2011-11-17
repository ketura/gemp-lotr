package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an [ISENGARD] minion to make the Free Peoples player exert X companions, where X is
 * the number of these races you can spot in the fellowship: Dwarf, Elf, Man, and Wizard.
 */
public class Card3_063 extends AbstractOldEvent {
    public Card3_063() {
        super(Side.SHADOW, Culture.ISENGARD, "One of You Must Do This", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, CardType.MINION);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ISENGARD, CardType.MINION));
        int exertionCount = 0;
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Race.DWARF))
            exertionCount++;
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Race.ELF))
            exertionCount++;
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Race.MAN))
            exertionCount++;
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Race.WIZARD))
            exertionCount++;
        action.appendEffect(
                new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), exertionCount, exertionCount, CardType.COMPANION));
        return action;
    }
}
