package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharacterEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Search. Maneuver: Spot a [SAURON] Orc and 6 companions to wound a companion (except the Ring-bearer).
 * Do this once for each companion over 5.
 */
public class Card1_251 extends AbstractEvent {
    public Card1_251() {
        super(Side.SHADOW, Culture.SAURON, "A Host Avails Little", Phase.MANEUVER);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC))
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 6;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        int companionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
        int woundCount = companionCount - 5;
        for (int i = 0; i < woundCount; i++) {
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose companion", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER))) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.addEffect(
                                    new WoundCharacterEffect(companion));
                        }
                    });
        }
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }
}
