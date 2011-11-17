package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Search. Maneuver: Spot a [SAURON] Orc and 5 companions to make the Free Peoples player exert a companion
 * for each companion over 4.
 */
public class Card1_239 extends AbstractOldEvent {
    public Card1_239() {
        super(Side.SHADOW, Culture.SAURON, "All Thought Bent on It", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC))
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION) >= 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        int companionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
        for (int i = 0; i < companionCount - 4; i++)
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
