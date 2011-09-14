package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Search. Maneuver: Spot a [SAURON] Orc and 5 companions to make the Free Peoples player exert a companion
 * for each companion over 4.
 */
public class Card1_239 extends AbstractEvent {
    public Card1_239() {
        super(Side.SHADOW, Culture.SAURON, "All Thought Bent on It", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC))
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 5;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        int companionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
        int exertableCompanions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.canExert());
        int exertCount = Math.min(0, Math.min(companionCount - 4, exertableCompanions));
        if (exertCount > 0) {
            action.addEffect(
                    new ChooseActiveCardsEffect(game.getGameState().getCurrentPlayerId(), "Choose companions to exert", exertCount, exertCount, Filters.type(CardType.COMPANION), Filters.canExert()) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> companions) {
                            for (PhysicalCard companion : companions)
                                action.addEffect(new ExertCharacterEffect(playerId, companion));
                        }
                    });
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
